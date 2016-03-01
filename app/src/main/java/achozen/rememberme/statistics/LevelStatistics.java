package achozen.rememberme.statistics;

/**
 * Created by Achozen on 2016-03-01.
 */
public class LevelStatistics {

    public Enum getGameState() {
        return gameState;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getCorectness() {
        return corectness;
    }

    public void setGameState(Enum gameState) {
        this.gameState = gameState;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setCorectness(int corectness) {
        this.corectness = corectness;
    }

    private Enum gameState;
    private int timeLeft;
    private int corectness;
}
