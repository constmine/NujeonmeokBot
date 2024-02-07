package io.github.constmine.bot.commands.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class HelpCommand extends Command {

    private final EmbedBuilder embedBuilder = new EmbedBuilder()
            .setTitle("도움말")
            .setColor(Color.GREEN)
            .setDescription(
                    "명령어 리스트\n\n" +
                            "**#ping [#Ping, #핑]** 또는 **/ping**\n" +
                            "응답하는데 걸린 시간을 나타냅니다.\n\n" +
                            "**#pick <역할> [#Pick, #뽑기]** 또는 **/뽑기 <역할>** \n" +
                            "해당 서버의 사람들중 일부를 뽑습니다. <역할> 옵션을 통해서 해당 역할 범위로 좁힐 수 있습니다.\n\n" +
                            "**#disperse [#Disperse, #해산]** 또는 **/해산 ** \n" +
                            "속해 있는 보이스 채널의 유저를 모두 강퇴합니다.\n\n" +
                            "**#join [#Join, #입장]** 또는 **/join\n\n" +
                            "**"
            );

    public HelpCommand(MessageReceivedEvent event) {
        super(event);
    }

    public HelpCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void executeCommand() {
        event.getMessage().replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void executeSlashCommand() {
        slashEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
