package achozen.rememberme.engine;

import java.io.Serializable;
import java.util.ArrayList;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-02-27.
 */
public class GameInitializationData implements Serializable {
    private GameSize gameSize;
    private ArrayList<PointPosition> pointPositions;
    private ArrayList<PointPosition> patternPointPositions;

    public GameInitializationData(GameSize gameSize, ArrayList<PointPosition> pointPositions, ArrayList<PointPosition> patternPointPositions) {

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
}
