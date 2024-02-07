package io.github.constmine.bot.commands.command.music;

import io.github.constmine.bot.commands.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCommand extends Command {

    public SkipCommand(MessageReceivedEvent event) {
        super(event);
    }

    public SkipCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void executeCommand() {

        if(!event.getMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("해당 명령어를 수행하기 위해서는 보이스 채널에 접속해있어야합니다.").queue();;
            return;
        }

        if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("봇이 보이스 채널에 접속해 있지 않습니다.").queue();
            return;
        }

        PlayerManager.getINSTANCE().musicSkip(event.getGuildChannel().asTextChannel());
    }

    @Override
    public void executeSlashCommand() {

    }
}
