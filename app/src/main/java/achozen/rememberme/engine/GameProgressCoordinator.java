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

    public GameProgressCoordinator(Context context, GameProgressListener progressListener) {
        gameProgressListener = progressListener;
        GameProgressCoordinator.context = context;
    }

    public void startGame(GameMode gameMode) {
        initiateEnvironmentalVariables(gameMode);
        startNextLevel();
    }

    public void startNextLevel() {
        GameInitializationData initData = prepareNextLevel();
        if (initData == null) {
            //TODO handle here a ranking mode(e.g. display fragment with statistics)
            gameProgressListener.onTrainingFinished();
        } else {
            gameProgressListener.startNewLevel(initData);
        }
    }

    private void initiateEnvironmentalVariables(GameMode gameMode) {
        String difficulty;
        String gameSize;
        Difficulty currentDifficulty;
        if (gameMode == GameMode.RANKING) {
            currentGameSize = GameSize.SMALL;
            currentDifficulty = Difficulty.RANKING;
        } else {
            difficulty = PeferencesUtil.readFromPrefs(context, PeferencesUtil
                    .Preferences.DIFFICULTY);
            gameSize = PeferencesUtil.readFromPrefs(context, PeferencesUtil
                    .Preferences.SIZE);
            currentGameSize = GameSize.valueOf(gameSize);
            currentDifficulty = Difficulty.valueOf(difficulty);
        }


        levelDifficultyManager = new LevelDifficultyManager(currentDifficulty, currentGameSize);
    }

    private GameInitializationData prepareNextLevel() {
        ArrayList<PointPosition> pattern = levelDifficultyManager.createPatternForNextLevel();
        currentGameSize = levelDifficultyManager.getCurrentGameSize();
        if (pattern == null) {
            return null;
        }
        return new GameInitializationData(currentGameSize,
                PointsInitializer.generateLockPoints(currentGameSize), pattern);
    }
}
