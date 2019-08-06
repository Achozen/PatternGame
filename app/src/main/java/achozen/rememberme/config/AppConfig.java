package achozen.rememberme.config;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class AppConfig {

    private static AppConfig instance;

    @JsonField(name = "training_easy")
    public GameConf trainingEasy;
    @JsonField(name = "training_medium")
    public GameConf trainingMedium;
    @JsonField(name = "training_hard")
    public GameConf trainingHard;
    @JsonField(name = "ranking")
    public GameConf ranking;


    public static void init(AppConfig applicationConfig) {
        instance = applicationConfig;
    }

    public static AppConfig getInstance() {
        return instance;
    }

    @JsonObject
    public static class GameConf {
        @JsonField
        public ArrayList<Integer> medium = new ArrayList<>();
        @JsonField
        public ArrayList<Integer> big = new ArrayList<>();
        @JsonField
        public ArrayList<Integer> small = new ArrayList<>();
    }
}
