package io.github.constmine.bot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ManageDiscordToken {

    private String DiscordBotToken;

    public ManageDiscordToken() {
        settingToken();
    }

    private void settingToken() {
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("Application.propertise")) {
            Properties properties = new Properties();
            properties.load(input);
            DiscordBotToken = properties.getProperty("token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDiscordBotToken() {
        return DiscordBotToken;
    }


}
