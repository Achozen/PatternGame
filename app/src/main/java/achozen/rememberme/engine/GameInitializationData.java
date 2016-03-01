package achozen.rememberme.engine;

import java.util.ArrayList;

/**
 * Created by Achozen on 2016-02-27.
 */
public class GameInitializationData {
    private GameSize gameSize;
    private ArrayList<PointPosition> pointConnections;

    public GameInitializationData(GameSize gameSize, ArrayList<PointPosition> pointConnections) {

        this.gameSize = gameSize;
        this.pointConnections = pointConnections;
    }
    public GameSize getGameSize() {
        return gameSize;
    }

    public ArrayList<PointPosition> getPointConnections() {
        return pointConnections;
    }
}
