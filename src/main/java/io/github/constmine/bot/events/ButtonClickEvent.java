package io.github.constmine.bot.events;

import io.github.constmine.bot.commands.command.music.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ButtonClickEvent extends ListenerAdapter {

    private static final String YOUTUBE_URL_PATTERN =
            "(?i)^(https?://)?(www\\.)?(youtube\\.com\\S*|youtu\\.be\\S*)$";

    private static final Pattern pattern = Pattern.compile(YOUTUBE_URL_PATTERN);

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String Button_Id = event.getButton().getId();

        // 해당 버튼의 Id가 유튜브 Url 인지 확인
        if(isValidYouTubeUrl(event.getButton().getId())) {

            if(!event.getMember().getVoiceState().inAudioChannel()) {
                event.getChannel().sendMessage("소속해 있는 보이스 채널이 없습니다.").queue();
                return;
            }

            if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                audioManager.openAudioConnection(memberChannel);
            }

            PlayerManager.getINSTANCE().loadAndPlay(event.getChannel().asTextChannel(), Button_Id, event.getMember());

        }
    }

    public static boolean isValidYouTubeUrl(String url) {
        return pattern.matcher(url).matches();
    }

}

