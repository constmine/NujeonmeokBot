package io.github.constmine.bot.commands.command;

import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class DisperseCommand extends Command {

    public DisperseCommand(MessageReceivedEvent event) {
        super(event);
    }

    public DisperseCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void executeCommand() {
        AudioChannelUnion voiceChannel =  event.getMember().getVoiceState().getChannel();

        if(voiceChannel == null) {
            event.getMessage().reply("속해있는 보이스 채널이 없습니다.").queue();
            return;
        }


        List<Member> mems = voiceChannel.getMembers();


        for(Member mem : mems) {
            event.getGuild().kickVoiceMember(mem).queue();
        }


    }

    @Override
    public void executeSlashCommand() {
        AudioChannelUnion voiceChannel =  slashEvent.getMember().getVoiceState().getChannel();

        if(voiceChannel == null) {
            slashEvent.reply("속해있는 보이스 채널이 없습니다.").queue();
            return;
        }

        List<Member> mems = voiceChannel.getMembers();
        for(Member mem : mems) {
            slashEvent.getGuild().kickVoiceMember(mem).queue();
        }
    }
}
