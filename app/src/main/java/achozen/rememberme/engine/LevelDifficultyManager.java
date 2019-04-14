package achozen.rememberme.engine;

import java.util.ArrayList;
import java.util.Arrays;

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

    //Numbers of links for EASY mode
    private ArrayList<Integer> easySmall = new ArrayList<>(Arrays.asList(3, 4, 4, 5, 5));
    private ArrayList<Integer> easyMedium = new ArrayList<>(Arrays.asList(5, 6, 6, 7, 7));
    private ArrayList<Integer> easyBig = new ArrayList<>(Arrays.asList(7, 8, 8, 8, 9));

    //Numbers of links for MEDIUM mode
    private ArrayList<Integer> mediumSmall = new ArrayList<>(Arrays.asList(4, 4, 5, 5, 6));
    private ArrayList<Integer> mediumMedium = new ArrayList<>(Arrays.asList(6, 7, 7, 8, 8));
    private ArrayList<Integer> mediumBig = new ArrayList<>(Arrays.asList(8, 9, 9, 10, 10));

    //Numbers of links for HARD mode
    private ArrayList<Integer> hardSmall = new ArrayList<>(Arrays.asList(7, 7, 8, 8, 9));
    private ArrayList<Integer> hardMedium = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14));
    private ArrayList<Integer> hardBig = new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18));

    //Number of links in RANKING MODE
    //private ArrayList<Integer> rankingMedium = new ArrayList<>(Arrays.asList(5, 6, 6, 7, 7));
    //private ArrayList<Integer> rankingBig = new ArrayList<>(Arrays.asList(7, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 15, 16, 17, 18));
    //private ArrayList<Integer> rankingSmall = new ArrayList<>(Arrays.asList(3, 4, 4, 5, 5));
    private ArrayList<Integer> rankingSmall = new ArrayList<>(Arrays.asList(3));
    private ArrayList<Integer> rankingMedium = new ArrayList<>(Arrays.asList(4));
    private ArrayList<Integer> rankingBig = new ArrayList<>(Arrays.asList(5));

    private ArrayList<Integer> rankingAll = new ArrayList<>();


    private static final int MAX_LEVEL_FOR_TRAINING = 5;
    private GameMode gameMode;
    private int currentLevel;

    private Difficulty difficulty;
    private GameSize gameSize;


    /**
     * Use this constructor for creating training games. For RANKED difficulty gameSize will be small by efault
     */
    public LevelDifficultyManager(Difficulty difficulty, GameSize gameSize) {
        if (difficulty == Difficulty.RANKING) {
            gameMode = RANKING;
            rankingAll.addAll(rankingSmall);
            rankingAll.addAll(rankingMedium);
            rankingAll.addAll(rankingBig);
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
        if (currentLevel > rankingMedium.size() + rankingSmall.size() + rankingBig.size()) {
            return null;
        }
        if (currentLevel <= rankingSmall.size()) {
            gameSize = SMALL;
        } else if (currentLevel <= rankingMedium.size() + rankingSmall.size()) {
            gameSize = MEDIUM;
        } else if (currentLevel <= rankingMedium.size() + rankingSmall.size() + rankingBig.size()) {
            gameSize = BIG;
        }

        return LevelBuilder.forceGenerateLevel(gameSize, createLinksBasedOnDifficulty());
    }

    private ArrayList<PointPosition> createNextTrainingLevel() {
        if (currentLevel >= MAX_LEVEL_FOR_TRAINING) {
            currentLevel = 0;
            return null;
        }
        return LevelBuilder.forceGenerateLevel(gameSize, createLinksBasedOnDifficulty());
    }


    private int createLinksBasedOnDifficulty() {
        switch (difficulty) {

            case EASY:
                if (gameSize == SMALL) {
                    return easySmall.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return easyMedium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return easyBig.get(currentLevel - 1);
                }
            case MEDIUM:
                if (gameSize == SMALL) {
                    return mediumSmall.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return mediumMedium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return mediumBig.get(currentLevel - 1);
                }
            case HARD:
                if (gameSize == SMALL) {
                    return hardSmall.get(currentLevel - 1);
                }
                if (gameSize == MEDIUM) {
                    return hardMedium.get(currentLevel - 1);
                }
                if (gameSize == BIG) {
                    return hardBig.get(currentLevel - 1);
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
