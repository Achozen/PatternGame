package achozen.rememberme.statistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public class GameStatistics {

    private int gameTime;
    private int scoredPoints;
    private int levelFinishedCounter;
    private LevelState levelState;

    public GameStatistics(LevelState levelState, int gameTime, int scoredPoints, int levelFinishedCounter) {
        this.levelState = levelState;
        this.gameTime = gameTime;
        this.scoredPoints = scoredPoints;
        this.levelFinishedCounter = levelFinishedCounter;
    }

    public int getScoredPoints() {
        return scoredPoints;
    }

    public int getGameTime() {

        return gameTime;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    public int getLevelFinishedCounter() {
        return levelFinishedCounter;
    }
}
