package achozen.rememberme.engine;

import java.util.ArrayList;

import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.statistics.LevelProgresState;
import achozen.rememberme.statistics.LevelStatistics;

/**
 * Created by Achozen on 2016-03-01.
 */
public class GameControler {

    private static GameProgressListener gameProgressListener;

    public GameControler(GameProgressListener progressListener) {
        gameProgressListener = progressListener;

    }
    /**
     * Compares the drawn pattern with expected pattern and notify GameProgressListener
     */
    public static void compareDrawnPattern(ArrayList<Point> drawnPoints, ArrayList<PointPosition>
            expectedPoints) {
        LevelStatistics levelStatistics = new LevelStatistics();
        if(drawnPoints.size() != expectedPoints.size()){
            levelStatistics.setGameState(LevelProgresState.FAILED);
            gameProgressListener.onLevelFinished(levelStatistics);
        }else {
            for(int i = 0; i<drawnPoints.size() ; i++){

                if(!(drawnPoints.get(i).getX() == expectedPoints.get(i).getX() &&
                        drawnPoints.get(i).getY() == expectedPoints.get(i).getY())){
                    levelStatistics.setGameState(LevelProgresState.FAILED);
                    gameProgressListener.onLevelFinished(levelStatistics);
                    return;
                }
            }
            levelStatistics.setGameState(LevelProgresState.SUCCEDED);
            gameProgressListener.onLevelFinished(levelStatistics);
        }
    }

}
