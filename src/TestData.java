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
            {a, n, a, a, a, a, a, a},
            {a, a, a, a, a, a, 3, a}
    };

    final int[][] blackMatrix = new int[blackCountMatrix.length][blackCountMatrix[0].length];

    //[RoomNumber][0->2] 0=NumBlacks 1=Width 2=Height
    final int[][] roomInfoMatrix;

    public TestData() {

        //Initialize Black Matrix(all white)
        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                blackMatrix[row][cell] = 0;
            }
        }

        //Count Number of Rooms
        int roomCount = 0;
        for (int[] row : roomMatrix) {
            for (int roomIndex : row) {
                if (roomCount < roomIndex) roomCount = roomIndex;
            }
        }
        roomCount++;

        //Create List with room Informations int[RoomNumber][0->2] 0=NumBlacks 1=Width 2=Height
        roomInfoMatrix = new int[roomCount][3];
        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if (blackCountMatrix[row][cell] != a) {
                    roomInfoMatrix[roomMatrix[row][cell]][0] = (blackCountMatrix[row][cell]);
                }
            }
        }


        //Save With and Height of every Room [roomNumber][0-1] 0=count 1=locked
        int[][] roomWidth = new int[roomCount][2];
        int[][] roomHeight = new int[roomCount][2];

        int prevCell = -1;
        int[] prevRow = new int[blackMatrix[0].length];
        Arrays.setAll(prevRow, v -> -1);

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                //Width
                if (prevCell != roomMatrix[row][cell] && prevCell != -1) {
                    roomWidth[prevCell][1] = 1;
                }
                if (roomWidth[roomMatrix[row][cell]][1] != 1) {
                    roomWidth[roomMatrix[row][cell]][0]++;
                    prevCell = roomMatrix[row][cell];
                }
                //Height
                if (prevRow[cell] != roomMatrix[row][cell] && prevRow[cell] != -1) {
                    roomHeight[prevRow[cell]][1] = 1;
                }
                if (roomHeight[roomMatrix[row][cell]][1] != 1) {
                    roomHeight[roomMatrix[row][cell]][0]++;
                }
            }
            prevRow = roomMatrix[row];
        }

        for (int room = 0; room < roomCount; room++) {
            roomInfoMatrix[room][1] = roomWidth[room][0];
            roomInfoMatrix[room][2] = (roomHeight[room][0] / roomWidth[room][0]);
        }

        System.out.println(Arrays.deepToString(roomInfoMatrix));

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

    public int[][] getRoomInfoMatrix() {
        return roomInfoMatrix;
    }

}
