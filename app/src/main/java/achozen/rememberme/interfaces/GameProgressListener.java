package achozen.rememberme.interfaces;

import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public interface GameProgressListener {
    void onGameLost(GameStatistics statistics);

    void onGameWin(GameStatistics statistics);

    void onGameSaveInstanceState(GameStatistics statistics);
}
