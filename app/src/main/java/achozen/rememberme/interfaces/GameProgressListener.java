package achozen.rememberme.interfaces;

import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.statistics.LevelStatistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public interface GameProgressListener {
    void onGameLost(GameStatistics statistics);

    void onGameWin(GameStatistics statistics);

    void onGameSaveInstanceState(GameStatistics statistics);

    //called after drawing wrong pattern but there is a time to draw it again
    void onLevelContinue(LevelStatistics statistics);
    //called when pattern was correctly drawn
    void onLevelFinished(LevelStatistics statistics);
}
