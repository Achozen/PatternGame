package achozen.rememberme.statistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public class GameStatistics {

    private String gameState;
    private String gameTime;
    private int scoredPoints;

    public GameStatistics(String gameState, String gameTime, int scoredPoints) {
        this.gameState = gameState;
        this.gameTime = gameTime;
        this.scoredPoints = scoredPoints;
    }


    public int getScoredPoints() {
        return scoredPoints;
    }

    public String getGamTime() {

        return gameTime;
    }

    public String getGameState() {

        return gameState;
    }
}
