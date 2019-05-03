package achozen.rememberme.engine;

import java.util.ArrayList;
import java.util.List;

import achozen.rememberme.analytics.AnalyticEvent;
import achozen.rememberme.config.AppConfig;
import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;

import static achozen.rememberme.enums.GameMode.RANKING;
import static achozen.rememberme.enums.GameMode.TRAINING;
import static achozen.rememberme.enums.GameSize.BIG;
import static achozen.rememberme.enums.GameSize.MEDIUM;
import static achozen.rememberme.enums.GameSize.SMALL;

/**
 * Created by Achozen on 2016-06-01.
 */
public class LevelDifficultyManager {
    //There will be two modes of playing this game:
    // 1 - Training - user will be able to pick exact level and size e.g mediumBig and play 5
    //     games on this lvl.
    // 2 - Ranking - user will be able to pick difficulty, and he will be forced to play through
    //     SMALL MEDIUM and HARD levels with the best time.
    // Each difficulty mode will be adjusting to be little harder each level;
    //Numbers of links for EASY mode
    //
    // MAX NUMBER OF LINKS FOR GAME SIZES ARE:
    // SMALL(3X3) - 9
    // MEDIUM(4X4) - 16
    // LARGE(5X5) - 25
    //
    //

    private static final int MAX_LEVEL_FOR_TRAINING = 5;
    private final List<Integer> rankingAll = new ArrayList<>();
    private GameMode gameMode;
    private int currentLevel;

    private Difficulty difficulty;
    private GameSize gameSize;
    private final AppConfig.GameConf rankedConf;
    private final AppConfig.GameConf easyTrainingConf;
    private final AppConfig.GameConf mediumTrainingConf;
    private final AppConfig.GameConf hardTrainingConf;


    /**
     * Use this constructor for creating training games. For RANKED difficulty gameSize will be small by efault
     */
    public LevelDifficultyManager(Difficulty difficulty, GameSize gameSize) {
        rankedConf = AppConfig.getInstance().ranking;
        easyTrainingConf = AppConfig.getInstance().training_easy;
        mediumTrainingConf = AppConfig.getInstance().training_medium;
        hardTrainingConf = AppConfig.getInstance().training_hard;
        if (difficulty == Difficulty.RANKING) {
            gameMode = RANKING;
            rankingAll.addAll(rankedConf.small);
            rankingAll.addAll(rankedConf.medium);
            rankingAll.addAll(rankedConf.big);
        } else {
            gameMode = TRAINING;
        }

        this.difficulty = difficulty;
        this.gameSize = gameSize;
    }

    public ArrayList<PointPosition> createPatternForNextLevel() {
        currentLevel++;
        switch (gameMode) {
            case TRAINING:
                return createNextTrainingLevel();
            case RANKING:
                return createNextRankingLevel();
        }
        return null;
    }

    private ArrayList<PointPosition> createNextRankingLevel() {
        if (currentLevel > rankedConf.medium.size() + rankedConf.small.size() + rankedConf.big.size()) {
            return null;
        }
        if (currentLevel <= rankedConf.small.size()) {
            gameSize = SMALL;
        } else if (currentLevel <= rankedConf.medium.size() + rankedConf.small.size()) {
            gameSize = MEDIUM;
        } else if (currentLevel <= rankedConf.medium.size() + rankedConf.small.size() + rankedConf.big.size()) {
            gameSize = BIG;
        }

        long generationTime = System.currentTimeMillis();
        int linksNumber = createLinksBasedOnDifficulty();
        ArrayList<PointPosition> points = LevelBuilder.forceGenerateLevel(gameSize, linksNumber);
        AnalyticEvent.patternGenerated(System.currentTimeMillis() - generationTime, gameSize, difficulty, linksNumber);
        return points;
    }

    private ArrayList<PointPosition> createNextTrainingLevel() {
        if (currentLevel >= MAX_LEVEL_FOR_TRAINING) {
            currentLevel = 0;
            return null;
        }

        long generationTime = System.currentTimeMillis();
        int linksNumber = createLinksBasedOnDifficulty();
        ArrayList<PointPosition> points = LevelBuilder.forceGenerateLevel(gameSize, linksNumber);
        AnalyticEvent.patternGenerated(System.currentTimeMillis() - generationTime, gameSize, difficulty, linksNumber);
        return points;
    }


    private int createLinksBasedOnDifficulty() {
        switch (difficulty) {

            case EASY:
                if (gameSize == SMALL) {
                    return easyTrainingConf.small.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return easyTrainingConf.medium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return easyTrainingConf.big.get(currentLevel - 1);
                }
            case MEDIUM:
                if (gameSize == SMALL) {
                    return mediumTrainingConf.small.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return mediumTrainingConf.medium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return mediumTrainingConf.big.get(currentLevel - 1);
                }
            case HARD:
                if (gameSize == SMALL) {
                    return hardTrainingConf.small.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return hardTrainingConf.medium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return hardTrainingConf.big.get(currentLevel - 1);
                }
            case RANKING:
                return rankingAll.get(currentLevel - 1);

        }
        return 0;
    }

    public GameSize getCurrentGameSize() {
        return gameSize;
    }
}
