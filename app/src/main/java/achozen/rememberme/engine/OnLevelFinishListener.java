package achozen.rememberme.engine;

import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-06-07.
 */
public interface OnLevelFinishListener {

    void onLevelFinished(GameStatistics gameStatistics);
}
