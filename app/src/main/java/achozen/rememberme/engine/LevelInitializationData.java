package achozen.rememberme.engine;

import java.io.Serializable;
import java.util.ArrayList;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;
import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public class LevelInitializationData implements Serializable {
    private GameStatistics gameStatistics;
    private GameSize gameSize;
    private ArrayList<PointPosition> pointPositions;
    private ArrayList<PointPosition> patternPointPositions;

    public LevelInitializationData(GameSize gameSize, ArrayList<PointPosition> pointPositions, ArrayList<PointPosition> patternPointPositions, GameStatistics gameStatistics) {
        this.gameStatistics = gameStatistics;
        this.gameSize = gameSize;
        this.pointPositions = pointPositions;
        this.patternPointPositions = patternPointPositions;
    }

    public GameSize getGameSize() {
        return gameSize;
    }

    public ArrayList<PointPosition> getPointPositions() {
        return pointPositions;
    }

    public ArrayList<PointPosition> getPatternPointPositions() {
        return patternPointPositions;
    }

    public GameStatistics getGameStatistics() {
        return gameStatistics;
    }
}
