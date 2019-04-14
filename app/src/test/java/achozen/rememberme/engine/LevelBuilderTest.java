package achozen.rememberme.engine;

import org.junit.Test;

import java.util.ArrayList;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;

import static org.junit.Assert.assertTrue;

/**
 * Created by Achozen on 2016-02-29.
 */
public class LevelBuilderTest {
    @Test
    public void generateFirstPointShouldCreateCorrectPoint() {

        for (int i = 0; i < 50; i++) {
            PointPosition pointPosition = LevelBuilder.generateFirstPoint(3, 3);
            System.out.println("generateFirstPointShouldCreateCorrectPoint Point number " + i + "" +
                    " X : " + pointPosition.getColumn() + " Y " +
                    ": " + pointPosition.getRow());
            boolean correctHorizontally = pointPosition.getColumn() <= 2;
            boolean correctVertically = pointPosition.getRow() <= 2;
            assertTrue(correctHorizontally && correctVertically);
        }


    }

    @Test
    public void forceGenerateLevelSholdGenerateLevel() {
        ArrayList<achozen.rememberme.interfaces.PointPosition> forceGene = LevelBuilder.forceGenerateLevel(GameSize.BIG, 5);

        for (PointPosition position : forceGene) {
            System.out.println("TAGTAG Generated point [" + position.getColumn() + "," + position.getRow() + "]");
        }
        assertTrue(true);

    }

    @Test
    public void generateLevelSholdGenerateLevel() {


        for (int i = 0; i < 50; i++) {
            ArrayList<PointPosition> generated = LevelBuilder.generateLevel(3, 3, 5);

            for (PointPosition position : generated) {
                System.out.println("TAGTAG Generated point [" + position.getColumn() + "," + position
                        .getRow()+ "]");

            }
            System.out.println("---------");
        }


        assertTrue(true);
    }


    @Test
    public void generateNextPointShouldGenerateValidNextPoint() {
        for (int i = 0; i < 50; i++) {
            PointPosition pointPosition = LevelBuilder.generateNextPoint(3, 3, new Point(1,1, 1));

            System.out.println("TAGTAG Generated next point [" + pointPosition.getColumn() + "," +
                    pointPosition.getRow() + "]");
        }

    }

    @Test
    public void createVirtualPointArrayAroundThePointTest() {
        for (int i = 0; i < 10; i++) {
            ArrayList<PointPosition> virtualArray = LevelBuilder
                    .createVirtualPointArrayAroundThePoint(new Point(1,1, 2));

            for (int j = 0; j < virtualArray.size(); j++) {

                System.out.println("TAGTAG Generated array [" + virtualArray.get(j).getColumn() + ","
                        + virtualArray.get(j).getRow()+ "]");
            }
            System.out.println("--------------");
        }
    }

    @Test
    public void createRealPointsTest() {
        for (int i = 0; i < 10; i++) {
            ArrayList<PointPosition> virtualArray = LevelBuilder.createRealPoints(4, 4);

            for (int j = 0; j < virtualArray.size(); j++) {

                System.out.println("TAGTAG Generated array [" + virtualArray.get(j).getColumn() + ","
                        + virtualArray.get(j).getRow() + "]");
            }
            System.out.println("--------------");
        }
    }


}
