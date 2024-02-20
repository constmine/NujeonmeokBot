package io.github.constmine.bot.commands.command.music;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.constmine.bot.APITokenManage;
import io.github.constmine.bot.tools.KeyEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private long trackPosition;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackURL, Member client) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {

                musicManager.scheduler.queue(audioTrack);

                AudioTrackInfo info = audioTrack.getInfo();
                try {
                    Document doc = Jsoup.connect(info.uri).get();

                    Elements thumbnailElements = doc.select("meta[property=og:image]");
                    String thumbnailUrl = thumbnailElements.attr("content");

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(":headphones: 추가된 곡").
                            setDescription("### ** [" + info.title + "](" + info.uri + ") **").
                            setThumbnail(thumbnailUrl).
                            addField("곡 길이"
                                    , info.length / 1000 / 60 + "분" + info.length / 1000 % 60 + "초", true).
                            addField("신청자", client.getAsMention() , true).
                            addField("제작자", info.author, true).
                            setColor(Color.GREEN);

                    textChannel.sendMessageEmbeds(builder.build()).queue();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));

                    try {
                        AudioTrackInfo info = tracks.get(0).getInfo();

                        Document doc = Jsoup.connect(info.uri).get();
                        Elements thumbnailElements = doc.select("meta[property=og:image]");
                        String thumbnailUrl = thumbnailElements.attr("content");

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle(":headphones: 추가된 곡").
                                setDescription("### ** [" + info.title + "](" + info.uri + ") **").
                                setThumbnail(thumbnailUrl).
                                addField("곡 길이"
                                        , info.length / 1000 / 60 + "분" + info.length / 1000 % 60 + "초", true).
                                addField("신청자", client.getAsMention(), true).
                                addField("제작자", info.author, true).
                                setColor(Color.YELLOW);

                        textChannel.sendMessageEmbeds(builder.build()).queue();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void noMatches() {
                try {
                    YouTube youTube = new YouTube.Builder(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            httpRequest -> {})
                            .setApplicationName("NujeonmeokBot")
                            .build();

                    List<String> videoIds = searchVideos(youTube, trackURL);

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("** 해당 곡을 찾을 수 없습니다. **").
                            setDescription("아래 추천 리스트에서 원하시는 곡을 골라주세요!").
                            setColor(Color.YELLOW);

                    ArrayList<Button> buttons = new ArrayList<>();

                    for (int i = 0; i < videoIds.size(); i++) {
                        String videoId = videoIds.get(i);

                        String videoTitle = getVideoTitle(youTube, videoId);
                        String thumbnailUrl = getVideoThumbnailUrl(youTube, videoId);

                        KeyEmoji keyEmoji = KeyEmoji.getByIndex(i);

                        builder.addField(keyEmoji.getEmoji() + " " + videoTitle, (i+1) + "번곡 버튼을 눌러주세요", false);
                        builder.addBlankField(false);

                        buttons.add(Button.of(ButtonStyle.SUCCESS, "https://www.youtube.com/watch?v=" + videoId, (i+1 + "번곡")));
                    }

                    textChannel.sendMessageEmbeds(builder.build())
                            .setActionRow(buttons)
                            .queue();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("노래 로드에 실패하였습니다 : " + e.getMessage());
            }
        });
    }

    private static String getVideoThumbnailUrl(YouTube youtube, String videoId) throws IOException {
        YouTube.Videos.List videoListRequest = youtube.videos().list(Collections.singletonList("snippet"));
        videoListRequest.setKey(APITokenManage.getToken("youtube_API_Token"));
        videoListRequest.setId(Collections.singletonList(videoId));

        VideoListResponse response = videoListRequest.execute();
        List<Video> videos = response.getItems();

        if (videos != null && !videos.isEmpty()) {
            return videos.get(0).getSnippet().getThumbnails().getDefault().getUrl();
        }

        return "해당 이미지를 찾을 수 없습니다.";
    }

    private static String getVideoTitle(YouTube youtube, String videoId) throws IOException {
        YouTube.Videos.List videoListRequest = youtube.videos().list(Collections.singletonList("snippet"));
        videoListRequest.setKey(APITokenManage.getToken("youtube_API_Token"));
        videoListRequest.setId(Collections.singletonList(videoId));

        VideoListResponse response = videoListRequest.execute();
        List<Video> videos = response.getItems();

        if (videos != null && !videos.isEmpty()) {
            return videos.get(0).getSnippet().getTitle();
        }

        return "해당 영상이 없습니다.";
    }

    private static List<String> searchVideos(YouTube youtube, String searchTerm) throws IOException {
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));

        search.setKey(APITokenManage.getToken("youtube_API_Token"));
        search.setQ(searchTerm);
        search.setType(Collections.singletonList("video"));
        search.setMaxResults(5L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResults = searchResponse.getItems();

        List<String> videoIds = new ArrayList<>();
        for (SearchResult result : searchResults) {
            String videoId = result.getId().getVideoId();
            videoIds.add(videoId);
        }

        return videoIds;
    }

    public void musicStop(TextChannel textChannel) {
        final GuildMusicManager musicManager = getMusicManager(textChannel.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() != null) {
            trackPosition = audioPlayer.getPlayingTrack().getPosition();
            audioPlayer.stopTrack();
        }
    }

    public void musicSkip(TextChannel textChannel) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        musicManager.scheduler.nextTrack();
    }

    public void musicRestart(TextChannel textChannel) {
        final GuildMusicManager musicManager = getMusicManager(textChannel.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() != null) {
            AudioTrack playingTrack = audioPlayer.getPlayingTrack();
            playingTrack.setPosition(trackPosition);
            musicManager.scheduler.queue(playingTrack);
            audioPlayer.playTrack(playingTrack);
        }
    }

    public static PlayerManager getINSTANCE() {

        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

}
