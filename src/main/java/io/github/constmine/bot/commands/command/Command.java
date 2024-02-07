package io.github.constmine.bot.commands.command;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public abstract class Command {

    protected MessageReceivedEvent event;
    protected SlashCommandInteractionEvent slashEvent;

    protected MessageChannelUnion channel;


    public Command(MessageReceivedEvent event) {
        this.event = event;
        initialize();
    }

    public Command(SlashCommandInteractionEvent event) {
        this.slashEvent = event;
        initialize();
    }

    protected void initialize() {
        if(event != null) {
            channel = event.getChannel();

        } else {
            channel = slashEvent.getChannel();
        }
    }


    public abstract void executeCommand();
    public abstract void executeSlashCommand();

    protected MessageCreateAction sendMessage(String text) {
        return channel.sendMessage(text);
    }


}
