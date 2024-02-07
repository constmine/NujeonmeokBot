package io.github.constmine.bot.commands.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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

                textChannel.sendMessage(
                        "추가중입니다.**\n" +
                             audioTrack.getInfo().title +
                             "** 제작자 **" +
                             audioTrack.getInfo().author + "**"
                ).queue();


            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));
                    textChannel.sendMessage(
                            "추가중입니다.**\n" +
                                    tracks.get(0).getInfo().title +
                                    "** 제작자 **" +
                                    tracks.get(0).getInfo().author + "**"
                    ).queue();
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
