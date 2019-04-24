package achozen.rememberme.config;

import java.util.ArrayList;

public class AppConfig {
    private static AppConfig instance;
    public GameConf training_easy;
    public GameConf training_medium;
    public GameConf training_hard;
    public GameConf ranking;


    public static void init(AppConfig applicationConfig) {
        instance = applicationConfig;
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public class GameConf {
        public ArrayList<Integer> medium = new ArrayList<>();
        public ArrayList<Integer> big = new ArrayList<>();
        public ArrayList<Integer> small = new ArrayList<>();
    }
}
