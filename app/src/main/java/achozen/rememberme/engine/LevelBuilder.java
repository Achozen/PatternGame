package achozen.rememberme.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;

/**
 * Remember that level builder is able to generate non-symetric levels like 4x6
 */
public class LevelBuilder {
    private List<PointPosition> result = Collections.synchronizedList(new ArrayList<>());
    public static int failedAttempts = 0;


    public List<PointPosition> forceGenerateLevel(GameSize gameSize, int
            numberOfLinks) {
        int width = 0;
        int height = 0;

        switch (gameSize) {
            case SMALL:
                width = 3;
                height = 3;
                break;
            case MEDIUM:
                width = 4;
                height = 4;
                break;
            case BIG:
                width = 5;
                height = 5;
                break;

        }

        List<PointPosition> generatedLevel = generateLevel(width, height, numberOfLinks);
        while (generatedLevel.size() != numberOfLinks + 1) {
            failedAttempts++;
            generatedLevel = generateLevel(width, height, numberOfLinks);
        }
        return generatedLevel;

    }


    List<PointPosition> generateLevel(int width, int height, int numberOfLinks) {
        result.clear();
        PointPosition startPoint = generateFirstPoint(width, height);
        result.add(startPoint);

        PointPosition nextPoint = generateNextPoint(width, height, startPoint);
        for (int i = 0; i < numberOfLinks; i++) {
            result.add(nextPoint);
            nextPoint = generateNextPoint(width, height, nextPoint);
            if (nextPoint == null) {
                return result;
            }
        }
        return result;
    }

    PointPosition generateFirstPoint(int width, int height) {
        Random randomGenerator = new Random();
        Point point = null;
        int id = 0;
        for (int idx = 1; idx <= 10; ++idx) {
            point = new Point(id++, showRandomInteger(0, width - 1, randomGenerator),
                    showRandomInteger(0, height - 1, randomGenerator));
        }

        return point;
    }

    //tested
    PointPosition generateNextPoint(int width, int height, PointPosition
            previousPointPosition) {

        List<PointPosition> possibleMoves = calculatePossibleMoves(width, height,
                previousPointPosition);

        Random randomGenerator = new Random();
        if (possibleMoves.size() < 1) {
            return null;
        }
        int generatedIndex = showRandomInteger(0, possibleMoves.size() - 1, randomGenerator);

        return possibleMoves.get(generatedIndex);
    }

    List<PointPosition> calculatePossibleMoves(int width, int height,
                                               PointPosition
                                                       previousPointPosition) {

        ArrayList<PointPosition> possibleMoves;
        possibleMoves = obtainPointsAroundPoint(width, height, previousPointPosition);
        return excludePreviouslyGeneratedPoints(possibleMoves, result);
    }

    ArrayList<PointPosition> obtainPointsAroundPoint(int width, int height,
                                                     PointPosition
                                                             previousPointPosition) {
        ArrayList<PointPosition> pointsAround;

        ArrayList<PointPosition> virtualPointsAround = createVirtualPointArrayAroundThePoint
                (previousPointPosition);
        ArrayList<PointPosition> realPoints = createRealPoints(width, height);

        pointsAround = obtainTheSamePoints(virtualPointsAround, realPoints);

        return pointsAround;

    }

    ArrayList<PointPosition> obtainTheSamePoints(ArrayList<PointPosition>
                                                         virtualPointsAround,
                                                 ArrayList<PointPosition>
                                                         realPoints) {
        ArrayList<PointPosition> theSamePoints = new ArrayList<>();


        for (PointPosition virtualPoint : virtualPointsAround) {

            for (PointPosition realPoint : realPoints) {

                if (virtualPoint.getColumn() == realPoint.getColumn() && virtualPoint.getRow() ==
                        realPoint
                                .getRow()) {
                    theSamePoints.add(virtualPoint);
                }
            }
        }

        return theSamePoints;

    }

    // tested - working
    ArrayList<PointPosition> createVirtualPointArrayAroundThePoint(PointPosition
                                                                           previousPointPosition) {

        ArrayList<PointPosition> pointsAround = new ArrayList<>();
        int midX = previousPointPosition.getColumn();
        int midY = previousPointPosition.getRow();

        int id = 0;

        for (int i = midX - 1; i <= midX + 1; i++) {
            for (int j = midY - 1; j <= midY + 1; j++) {
                if (!(i == midX && j == midY))
                    pointsAround.add(new Point(id++, i, j));
            }
        }

        return pointsAround;
    }

    //tested
    ArrayList<PointPosition> createRealPoints(int width, int height) {

        ArrayList<PointPosition> realPoints = new ArrayList<>();

        int id = 0;
        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                realPoints.add(new Point(id++, i, j));
            }
        }
        return realPoints;
    }


    // method takes all possible moves via parameter and excludes moves to points that was used
    // before
    List<PointPosition> excludePreviouslyGeneratedPoints
    (ArrayList<PointPosition> possibleMoves, List<PointPosition> listToExclude) {

        ArrayList<PointPosition> possibleMovesResult = new ArrayList<>();
        possibleMovesResult.addAll(possibleMoves);
        for (PointPosition previouslyGenerated : listToExclude) {

            for (int i = 0; i < possibleMovesResult.size(); i++) {
                if (possibleMovesResult.get(i).getColumn() == previouslyGenerated.getColumn() &&
                        possibleMovesResult.get(i).getRow() == previouslyGenerated.getRow()) {
                    possibleMovesResult.remove(possibleMovesResult.get(i));
                }
            }
        }

        return possibleMovesResult;
    }

    int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        return (int) (fraction + aStart);

    }
}