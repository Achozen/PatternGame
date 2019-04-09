package achozen.rememberme;

import java.util.ArrayList;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.engine.Point;
import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-05-26.
 */
public class PointsInitializer {
    public static ArrayList<PointPosition> generateLockPoints(GameSize gameSize){
        switch(gameSize){
            case SMALL:
                return preparePointsForSmallSize();
            case MEDIUM:
                return preparePointsForMediumSize();
            case BIG:
                return preparePointsForBigSize();
        }
        return preparePointsForSmallSize();
    }

    private static ArrayList<PointPosition> preparePointsForSmallSize(){
        return generatePointList(3,3);
    }
    private static ArrayList<PointPosition> preparePointsForMediumSize(){

        return generatePointList(4,4);
    }
    private static ArrayList<PointPosition> preparePointsForBigSize(){

        return generatePointList(5,5);
    }

    private static ArrayList<PointPosition> generatePointList(int numbOfPointsInCol,int numbOfPointsInRow){
        ArrayList<PointPosition> result = new ArrayList<>();
        int id=0;
        for(int i=0;i<numbOfPointsInCol;i++){
            for(int z=0;z<numbOfPointsInRow;z++){
                result.add(new Point(id++,z,i));
            }
        }
        return result;
    }
}
