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

    final int n=-(roomMatrix.length*roomMatrix[0].length+1);//No Blocks!

    // 0=Any number of Blocks , n=no blocks allowed in complete room(stronger than any)
    final int[][] blackCountMatrix = {
            {0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 3, 0}
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

        for (int value : countableRoomMatrix) {
            value = 0;

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
