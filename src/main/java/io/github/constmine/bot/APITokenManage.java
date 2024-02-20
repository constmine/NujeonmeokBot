package io.github.constmine.bot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APITokenManage {

    private static APITokenManage INSTANCE;

    private APITokenManage() {

    }

    public static String getToken(String Key) {
        try(InputStream input = getINSTANCE().getClass().getClassLoader().getResourceAsStream("Application.propertise")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(Key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static APITokenManage getINSTANCE() {
        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            return new APITokenManage();
        }
    }


}
