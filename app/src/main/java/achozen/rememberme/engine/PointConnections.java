package achozen.rememberme.engine;

/**
 * Created by Achozen on 2016-02-27.
 */
public class PointConnections {
    private PointPosition firstPointPosition;
    private PointPosition secondPointPosition;


    public PointPosition getFirstPointPosition() {
        return firstPointPosition;
    }

    public PointPosition getSecondPointPosition() {
        return secondPointPosition;
    }

    public PointConnections(PointPosition firstPointNumber, PointPosition secondPointNumber) {
        this.firstPointPosition = firstPointNumber;
        this.secondPointPosition = secondPointNumber;

    }

    public boolean validate(){
        if(!(isNotTheSame() && isInRange())){
          return false;
        }
        return true;
    }
    private boolean isNotTheSame(){

        return!(firstPointPosition.getX() == secondPointPosition.getX() && firstPointPosition.getY() == secondPointPosition.getY());
    }

    private boolean isInRange(){

        return isVereticallyCorrect() || isHorizontallyCorrect();
    }

    private boolean isHorizontallyCorrect() {
        if(firstPointPosition.getY() == secondPointPosition.getY()){
            if(Math.abs(firstPointPosition.getX() - secondPointPosition.getX()) == 1)
                return true;
        }
        return false;
    }


    private boolean isVereticallyCorrect() {
        if(firstPointPosition.getX() == secondPointPosition.getX()){
            if(Math.abs(firstPointPosition.getY() - secondPointPosition.getY()) == 1)
                return true;
        }
        return false;
    }

}
