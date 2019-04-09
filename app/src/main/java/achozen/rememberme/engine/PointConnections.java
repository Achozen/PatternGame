package achozen.rememberme.engine;

import achozen.rememberme.interfaces.PointPosition;

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

        return!(firstPointPosition.getColumn() == secondPointPosition.getColumn() && firstPointPosition.getRow() == secondPointPosition.getRow());
    }

    private boolean isInRange(){

        return isVereticallyCorrect() || isHorizontallyCorrect();
    }

    private boolean isHorizontallyCorrect() {
        if(firstPointPosition.getRow() == secondPointPosition.getRow()){
            if(Math.abs(firstPointPosition.getColumn() - secondPointPosition.getColumn()) == 1)
                return true;
        }
        return false;
    }


    private boolean isVereticallyCorrect() {
        if(firstPointPosition.getColumn() == secondPointPosition.getColumn()){
            if(Math.abs(firstPointPosition.getRow() - secondPointPosition.getRow()) == 1)
                return true;
        }
        return false;
    }

}
