package achozen.rememberme.statistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public class GameStatistics {

    private GameState gameState;
    private int gameTime;
    private int scoredPoints;

    public GameStatistics(GameState gameState, int gameTime, int scoredPoints) {
        this.gameState = gameState;
        this.gameTime = gameTime;
        this.scoredPoints = scoredPoints;
    }


    public int getScoredPoints() {
        return scoredPoints;
    }

    public int getGameTime() {

        return gameTime;
    }

    public GameState getGameState() {

        return gameState;
    }
}
