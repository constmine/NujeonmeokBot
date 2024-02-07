package io.github.constmine.bot.commands.command.music;

import io.github.constmine.bot.commands.command.Command;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand extends Command {

    public PlayCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    public PlayCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void executeCommand() {
        if(!event.getMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("소속해 있는 보이스 채널이 없습니다.").queue();;
            return;
        }

        if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        String link = String.join(" ", event.getMessage().getContentDisplay().replace("#play", ""));
        if(!isUrl(link)) {
            link = "ytsearch:" + link + "노래";
        }

        System.out.println(link);

        PlayerManager.getINSTANCE().loadAndPlay(event.getChannel().asTextChannel(), link);
    }

    public boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public void executeSlashCommand() {

    }
}
