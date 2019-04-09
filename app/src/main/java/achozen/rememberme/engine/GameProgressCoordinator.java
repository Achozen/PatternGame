package achozen.rememberme.engine;

import android.content.Context;

import java.util.ArrayList;

import achozen.rememberme.PointsInitializer;
import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-05-28.
 */
public class GameProgressCoordinator {

    private static GameProgressListener gameProgressListener;
    private static LevelDifficultyManager levelDifficultyManager;
    private static Context context;
    private static GameSize currentGameSize;
    private static Difficulty currentDifficulty;
    private static GameMode currentGameMode;

    public  GameProgressCoordinator(Context con, GameProgressListener progressListener) {
        gameProgressListener = progressListener;
        context = con;
    }

    public void startGame(GameMode gameMode) {
        initiateEnvironmentalVariables(gameMode);
        startNextLevel();

    }

    public void startNextLevel(){
        switch (currentGameMode) {
            case TRAINING:
                GameInitializationData initData = prepareNextLevel();
                if(initData == null){
                    gameProgressListener.onTrainingFinished();
                }else{
                    gameProgressListener.startNewLevel(initData);
                }

                break;
            case RANKING:

                break;
        }
    }

    private void  initiateEnvironmentalVariables(GameMode gameMode){
        currentGameMode = gameMode;
        String difficulty = PeferencesUtil.readFromPrefs(context, PeferencesUtil
                .Preferences.DIFFICULTY);
        String gameSize = PeferencesUtil.readFromPrefs(context, PeferencesUtil
                .Preferences.SIZE);

        currentGameSize = GameSize.valueOf(gameSize);
        currentDifficulty = Difficulty.valueOf(difficulty);
        levelDifficultyManager = new LevelDifficultyManager(currentDifficulty,currentGameSize);
    }

    private GameInitializationData prepareNextLevel(){
        ArrayList<PointPosition> pattern = levelDifficultyManager.createPatternForNextLevel();
        if(pattern == null){
            return null;
        }
        return new GameInitializationData(currentGameSize,
                        PointsInitializer.generateLockPoints(currentGameSize), pattern);
    }
}
