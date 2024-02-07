package io.github.constmine.bot.commands.command;

import io.github.constmine.bot.tools.ManageMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PickCommand extends Command{

    private List<Member> members;

    public PickCommand(MessageReceivedEvent event) {
        super(event);
        members = ManageMember.getAllMember(event.getGuild());
    }

    public PickCommand(SlashCommandInteractionEvent event) {
        super(event);
        members = ManageMember.getAllMember(Objects.requireNonNull(event.getGuild()));
    }

    @Override
    public void executeCommand() {
        Random random = new Random();
        int random_num = random.nextInt(members.size());
        EmbedBuilder eb = new EmbedBuilder();

        for (int i = 0; i <= 100; i++) {
            if(members.get(random_num).getUser().isBot()) random_num = random.nextInt(members.size()); else break;
            if(i == 100) {
                sendMessage("잘못된 형식이거나 해당 역할에서 뽑을 인원이 없습니다. (봇 제외)").queue();
                return;
            }
        }

        eb.setColor(java.awt.Color.decode("#3498db")).setDescription("당첨!");
        MessageEmbed meb = eb.build();
        event.getMessage().reply(("**"
                + members.get(random_num).getUser().getEffectiveName()
                + "**")).addEmbeds(meb).queue();
    }


    public void executeCommand(String role_name) {
        members = ManageMember.getRoleMember(event.getGuild(), role_name);

        if(members == null) {
            event.getMessage().reply("잘못된 형식이거나 해당 역할에서 뽑을 인원이 없습니다. (봇 제외)").queue();
        } else {
            executeCommand();
        }
    }

    @Override
    public void executeSlashCommand() {

        Random random = new Random();
        int random_num = random.nextInt(members.size());
        EmbedBuilder eb = new EmbedBuilder();

        for (int i = 0; i <= 100; i++) {
            if(members.get(random_num).getUser().isBot()) random_num = random.nextInt(members.size()); else break;
            if(i == 100) {
                slashEvent.reply("잘못된 형식이거나 해당 역할에서 뽑을 인원이 없습니다. (봇 제외)").queue();
                return;
            }
        }


        eb.setColor(java.awt.Color.decode("#3498db")).setDescription("당첨!");
        MessageEmbed meb = eb.build();
        slashEvent.reply(("**"
                + members.get(random_num).getUser().getEffectiveName()
                + "**")).addEmbeds(meb).queue();
    }

    public void executeSlashCommand(Role role) {
        members = ManageMember.getRoleMember(slashEvent.getGuild(), role.getName());
        executeSlashCommand();
    }
}
