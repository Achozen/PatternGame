package achozen.rememberme.interfaces;

import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public interface GameProgressListener {
    void onRankedFinished(GameStatistics statistics);

    void startNewLevel(GameInitializationData gameInitializationData);

    void onTrainingFinished();

    void onGameSaveInstanceState(GameStatistics statistics);
}
