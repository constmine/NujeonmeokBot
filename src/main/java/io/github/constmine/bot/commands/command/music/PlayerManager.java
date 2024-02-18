package io.github.constmine.bot.commands.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void loadAndPlay(TextChannel textChannel,String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**" + audioTrack.getInfo().title + "**").
                        setDescription("**" + audioTrack.getInfo().title + "**").
                        addField("Field 1 ", "1", false).
                        addField("Field 2 ", "2", true).
                        setColor(Color.BLUE);


                textChannel.sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));

                    System.out.println(tracks.get(0).getInfo().uri);

                    YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), null)
                            .setApplicationName("MyBot")
                            .build();

                    try {
                        String videoId = extractVideoId(youtubeLink);

                        YouTube.Videos.List listRequest = youtube.videos().list(Collections.singletonList("snippet"));
                        listRequest.setId(Collections.singletonList(videoId));
                        VideoListResponse listResponse = listRequest.setKey(YOUTUBE_API_KEY).execute();
                        Video video = listResponse.getItems().get(0);

                        // 썸네일 가져오기
                        Thumbnail thumbnail = video.getSnippet().getThumbnails().getDefault();


                        EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("**" + tracks.get(0).getInfo().title + "**").
                            setDescription("**" + tracks.get(0).getInfo().title + "**").
                            setThumbnail("https://cdn.pixabay.com/photo/2017/08/01/22/38/flash-2568381_1280.jpg").
                            addField("Field 1 ", "1", false).
                            addField("Field 2 ", "2", true).
                            setColor(Color.YELLOW);

                    textChannel.sendMessageEmbeds(builder.build()).queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
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
