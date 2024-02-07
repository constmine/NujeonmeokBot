package io.github.constmine.bot.commands.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends Command {

    public PingCommand(MessageReceivedEvent event) {
        super(event);
    }

    public PingCommand(SlashCommandInteractionEvent event) {
        super(event);
    }


    @Override
    public void executeCommand() {
        long time = System.currentTimeMillis();
        event.getMessage().reply("**Pong!**")
                .flatMap(v ->
                        channel.sendMessageFormat("까지 하는데 %d ms 걸렸습니다.", System.currentTimeMillis() - time)
                ).queue();
    }

    @Override
    public void executeSlashCommand() {
        long time = System.currentTimeMillis();
        slashEvent.reply("**Pong!**")
                .flatMap(v ->
                        channel.sendMessageFormat("까지 하는데 %d ms 걸렸습니다.", System.currentTimeMillis() - time)
                ).queue();
    }

}

