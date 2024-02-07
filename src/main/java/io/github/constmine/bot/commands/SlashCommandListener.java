package io.github.constmine.bot.commands;

import io.github.constmine.bot.commands.command.*;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                PingCommand cmd_ping = new PingCommand(event);
                cmd_ping.executeSlashCommand();
                break;

            case "pick" :
                PickCommand cmd_pick = new PickCommand(event);
                try {
                    Role select_role = event.getOption("select_role").getAsRole();
                    cmd_pick.executeSlashCommand(select_role);
                } catch (NullPointerException e) {
                    cmd_pick.executeSlashCommand();
                }
                break;

            case "disperse":
                DisperseCommand cmd_disperse = new DisperseCommand(event);
                cmd_disperse.executeSlashCommand();
                break;

            case "join" :
                JoinCommand joinCommand = new JoinCommand(event);
                joinCommand.executeSlashCommand();
                break;

            case "help" :
            case "도움말":
                HelpCommand cmd_help = new HelpCommand(event);
                cmd_help.executeSlashCommand();
                break;
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        commands.add(
                Commands.slash("ping", "Pong을 해줍니다")
                        .addOption(OptionType.USER, "user", "해당 유저", false)
        );
        commands.add(
                Commands.slash("pick", "특정 유저(봇 제외)중 한명을 뽑습니다. /뽑기 <역할 군에서 뽑기>")
                        .addOption(OptionType.ROLE, "select_role", "해당 역할을 가진 유저중에서만 뽑습니다.", false)
        );
        commands.add(
                Commands.slash("해산", "속해 있는 보이스 채널의 유저를 모두 강퇴합니다. 사용 주의!!")
        );
        commands.add(
                Commands.slash("join", "속해 있는 보이스에 누전먹봇을 입장시킵니다.")
        );

        commands.add(Commands.slash("help", "명령어 목록을 보여줍니다"));
        commands.add(Commands.slash("도움말", "명령어 목록을 보여줍니다"));
        event.getGuild().updateCommands().addCommands(commands).queue();
    }
}
