package io.github.constmine.bot.commands.command;

import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends Command {

    public JoinCommand(MessageReceivedEvent event) {
        super(event);
    }

    public JoinCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void executeCommand() {
        AudioManager manager = event.getGuild().getAudioManager();
//        manager.setSpeakingMode(SpeakingMode.VOICE);
        manager.openAudioConnection(event.getMember().getVoiceState().getChannel());
    }

    @Override
    public void executeSlashCommand() {
        AudioManager manager = slashEvent.getGuild().getAudioManager();
        manager.setSpeakingMode(SpeakingMode.VOICE);
        manager.openAudioConnection(slashEvent.getMember().getVoiceState().getChannel());
    }

}
