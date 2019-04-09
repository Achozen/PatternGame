package achozen.rememberme.engine;

import android.util.Log;

import java.util.ArrayList;

import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.interfaces.PointPosition;

import static achozen.rememberme.enums.GameMode.*;
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
    int[] easySmall = {3,4,4,5,5};
    int[] easyMedium = {5,6,6,7,7};
    int[] easyBig = {7,8,8,8,9};

    //Numbers of links for MEDIUM mode
    int[] mediumSmall = {4,4,5,5,6};
    int[] mediumMedium = {6,7,7,8,8};
    int[] mediumBig = {8,9,9,10,10};

    //Numbers of links for HARD mode
    int[] hardSmall = {7,7,8,8,9};
    int[] hardMedium = {10,11,12,13,14};
    int[] hardBig = {14,15,16,17,18};

    private static final int MAX_LEVEL_FOR_TRAINING = 5;
    private GameMode gameMode;
    private int currentLevel;

    Difficulty difficulty;
    GameSize gameSize;


    /**
     * Use this constructor for creating training games
     */
    public LevelDifficultyManager(Difficulty difficulty, GameSize gameSize){
        gameMode = TRAINING;
        this.difficulty = difficulty;
        this.gameSize = gameSize;
    }

    /**
     * Use this constructor for creating ranked games
     */
    public LevelDifficultyManager(Difficulty difficulty){
        gameMode = RANKING;
        this.difficulty = difficulty;
    }

    public ArrayList<PointPosition> createPatternForNextLevel(){

        switch(gameMode){
            case TRAINING:
                 return createNextTrainingLevel();
            case RANKING:
                return createNextRankingLevel();
        }
        return null;
    }

    private ArrayList<PointPosition> createNextRankingLevel() {
        return null;
    }

    private ArrayList<PointPosition> createNextTrainingLevel() {
        if(currentLevel >= MAX_LEVEL_FOR_TRAINING){
            currentLevel = 0;
            return null;
        }
        return LevelBuilder.forceGenerateLevel(gameSize,createLinksBasedOnDifficulty());
    }


    private int createLinksBasedOnDifficulty(){
        currentLevel++;
        switch(difficulty){

            case EASY:
                if(gameSize == SMALL){
                    return easySmall[currentLevel-1];
                }
                if(gameSize == MEDIUM){
                    return easyMedium[currentLevel-1];
                }
                if(gameSize == BIG){
                    return easyBig[currentLevel-1];
                }
            case MEDIUM:
                if(gameSize == SMALL){
                    return mediumSmall[currentLevel-1];
                }
                if(gameSize == MEDIUM){
                    return mediumMedium[currentLevel-1];
                }
                if(gameSize == BIG){
                    return mediumBig[currentLevel-1];
                }
            case HARD:
                if(gameSize == SMALL){
                    return hardSmall[currentLevel-1];
                }
                if(gameSize == MEDIUM){
                    return hardMedium[currentLevel-1];
                }
                if(gameSize == BIG){
                    return hardBig[currentLevel-1];
                }
        }
        return 0;
    }

}
