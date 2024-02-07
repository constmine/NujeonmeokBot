package io.github.constmine.bot.commands;

import io.github.constmine.bot.commands.command.*;
import io.github.constmine.bot.commands.command.music.PlayCommand;
import io.github.constmine.bot.commands.command.music.RestartCommand;
import io.github.constmine.bot.commands.command.music.SkipCommand;
import io.github.constmine.bot.commands.command.music.StopCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        switch(message[0]) {
            case "#핑" :
            case "#Ping" :
            case "#ping" :
                PingCommand pingCommand = new PingCommand(event);
                pingCommand.executeCommand();
                break;

            case "#뽑기" :
            case "#Pick" :
            case "#pick" :
                PickCommand pickCommand = new PickCommand(event);
                pickCommand.executeCommand();
                break;

            case "#해산" :
            case "#Disperse" :
            case "#disperse" :
                DisperseCommand disperseCommand = new DisperseCommand(event);
                disperseCommand.executeCommand();
                break;

            case "#입장" :
            case "#Join" :
            case "#join" :
                JoinCommand joinCommand = new JoinCommand(event);
                joinCommand.executeCommand();
                break;

            case "#노래" :
            case "#Play" :
            case "#play" :
                PlayCommand playCommand = new PlayCommand(event);
                playCommand.executeCommand();
                break;

            case "#스킵" :
            case "#Skip" :
            case "#skip" :
                SkipCommand skipCommand = new SkipCommand(event);
                skipCommand.executeCommand();
                break;

            case "#멈춤" :
            case "#Stop" :
            case "#stop" :
                StopCommand stopCommand = new StopCommand(event);
                stopCommand.executeCommand();
                break;

            case "#재시작" :
            case "#restart" :
            case "#Restart" :
                RestartCommand restartCommand = new RestartCommand(event);
                restartCommand.executeCommand();
                break;

            case "#도움말" :
            case "#도움" :
            case "#Help" :
            case "#help" :
                HelpCommand helpCommand = new HelpCommand(event);
                helpCommand.executeCommand();
                break;
        }
        //버튼 출력방법
//            event.getChannel().sendMessage("축하")
//                    .setActionRow(
//                            Button.of(ButtonStyle.PRIMARY, "버튼1","button_1"),
//                            Button.of(ButtonStyle.DANGER, "버튼2","button_2"),
//                            Button.of(ButtonStyle.LINK, "https://www.google.com/search?q=%EA%B5%BF&sca_esv=596495099&tbm=isch&source=lnms&sa=X&ved=2ahUKEwiB7ou80c2DAxVwsVYBHdKWD4sQ_AUoAXoECAMQAw&biw=2048&bih=1023&dpr=1.25","button_3"),
//                            Button.of(ButtonStyle.SUCCESS, "버튼4","button_4"),
//                            Button.of(ButtonStyle.SECONDARY, "버튼5","button_5"))
//                    .queue();
//
//            // selection 선택방법
//            StringSelectMenu menu = StringSelectMenu.create("menu:id")
//                    .addOption("Hello World", "hw")
//                    .addOption("Hi", "h")
//                    .build();
//            event.getChannel().sendMessageComponents(
//                    ActionRow.of(menu)
//            ).queue();
//        event.getChannel().addReactionById(event.getMessageId(),  Emoji.fromUnicode("U+1F4CD")).queue();

    }



}



