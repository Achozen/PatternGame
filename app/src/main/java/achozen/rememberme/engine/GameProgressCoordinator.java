package achozen.rememberme.engine;

import android.content.Context;

import java.util.ArrayList;

import achozen.rememberme.PointsInitializer;
import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.interfaces.PointPosition;
import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.statistics.LevelState;
import achozen.rememberme.utils.TimerUtils;

/**
 * Created by Achozen on 2016-05-28.
 */
public class GameProgressCoordinator {

    private static GameProgressListener gameProgressListener;
    private static LevelDifficultyManager levelDifficultyManager;
    private static Context context;
    private static GameSize currentGameSize;
    private final GameMode gameMode;

    public GameProgressCoordinator(Context context, GameMode gameMode, GameProgressListener progressListener) {
        this.gameMode = gameMode;
        gameProgressListener = progressListener;
        GameProgressCoordinator.context = context;
        initiateEnvironmentalVariables(gameMode);
        TimerUtils.startTotalTimeMeasurement();
        startNextLevel(new GameStatistics(LevelState.SUCCESS, 0, 0, 0));
    }

    public void startNextLevel(final GameStatistics gameStatistics) {
        LevelInitializationData initData = prepareNextLevel(gameStatistics);
        if (initData == null) {
            if (gameMode == GameMode.RANKING) {
                gameProgressListener.onRankedFinished(gameStatistics);
                return;
            }
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

    private LevelInitializationData prepareNextLevel(GameStatistics gameStatistics) {
        ArrayList<PointPosition> pattern = levelDifficultyManager.createPatternForNextLevel();
        currentGameSize = levelDifficultyManager.getCurrentGameSize();
        if (pattern == null) {
            return null;
        }
        return new LevelInitializationData(currentGameSize,
                PointsInitializer.generateLockPoints(currentGameSize), pattern, gameStatistics);
    }
}
