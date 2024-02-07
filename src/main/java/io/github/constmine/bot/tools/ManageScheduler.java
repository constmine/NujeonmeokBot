package io.github.constmine.bot.tools;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ManageScheduler {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static int countdown = 5;

    public static void scheduleMessages(TextChannel channel) {
    }
}
