import java.util.Arrays;

/**
 * Created by flo on 10.02.16.
 */
public class TestData {


    final int[][] roomMatrix = {
            {0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 5, 6, 7, 7, 7},
            {3, 3, 3, 5, 8, 8, 8, 8},
            {3, 3, 3, 5, 9, 9, 9, 10},
            {4, 4, 4, 4, 9, 9, 9, 10}
    };

    final int n = Config.instance.n;
    final int a = Config.instance.a;

    final int[][] blackCountMatrix = {
            {a, 2, a, a, a, a, a, a},
            {a, a, a, a, a, a, a, a},
            {a, a, a, a, a, a, a, a},
            {a, a, a, a, a, a, a, a},
            {a, a, a, a, a, a, a, a},
            {a, a, a, a, a, a, 3, a}
    };

    final int[][] blackMatrix = new int[blackCountMatrix.length][blackCountMatrix[0].length];

    final int[] countableRoomMatrix;

    public TestData() {

        //Initialize Black Matrix
        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                blackMatrix[row][cell] = 0;
            }
        }

        //Create Countable Room Matrix
        int max = 0;
        for (int[] row : roomMatrix) {
            for (int cell : row) {
                if (max < cell) max = cell;
            }
        }

        countableRoomMatrix = new int[max + 1];
        Arrays.parallelSetAll( countableRoomMatrix, v -> a );

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if (blackCountMatrix[row][cell] != a) {
                    countableRoomMatrix[roomMatrix[row][cell]] = (blackCountMatrix[row][cell]);
                }
            }
        }

    }

    public int[][] getBlackCountMatrix() {
        return blackCountMatrix;
    }

    public int[][] getRoomMatrix() {
        return roomMatrix;
    }

    public int[][] getBlackMatrix() {
        return blackMatrix;
    }

    public int[] getCountableRoomMatrix() {
        return countableRoomMatrix;
    }

}
