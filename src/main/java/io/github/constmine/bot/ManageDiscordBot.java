package io.github.constmine.bot;

import io.github.constmine.bot.commands.MessageReceiveCommand;
import io.github.constmine.bot.commands.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;


public class ManageDiscordBot {

    public static void main(String[] args) {
//        ManageDiscordToken token = new ManageDiscordToken("token");
        ManageDiscordToken token = new ManageDiscordToken("testToken");
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES);


        JDABuilder.createDefault(token.getDiscordBotToken())
                .enableIntents(intents)
                .enableCache(CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.customStatus("명령어 기다리는 중 ..."))
                .addEventListeners(new MessageReceiveCommand(), new SlashCommandListener())
                .build();

    }

}
