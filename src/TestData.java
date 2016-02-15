import java.util.Arrays;

/**
 * Created by flo on 10.02.16.
 */
public class TestData {

    // i=ignored a=any
    final int a = Config.instance.a;

    //Matrix which gives information about Rooms and Room boundrys
    final int[][] roomMatrix = Config.instance.activeRoomMatrix;

    //Matrix which gives information about number of blacks in Rooms
    final Integer[][] blackCountMatrix = Config.instance.activeBlackCountMatrix;

    //Reduced Information gathered out of the roomMatrix and blackCountMatrix(roomMatrix and blackCountMatrix are there for better visualisation)
    //[RoomNumber][0->2] 0=NumBlacks 1=Width 2=Height 3=row start 4=cell start (start = left top corner of room)
    final int[][] roomInfoMatrix;

    final int[][] blackMatrix = new int[blackCountMatrix.length][blackCountMatrix[0].length];

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
        roomInfoMatrix = new int[roomCount][5];

        //Save With and Height of every Room [roomNumber][0-1] 0=count 1=locked
        int[][] roomWidth = new int[roomCount][2];
        int[][] roomHeight = new int[roomCount][2];

        int prevCell = -1;
        int[] prevRow = new int[blackMatrix[0].length];
        Arrays.parallelSetAll(prevRow, v -> -1);

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if(roomWidth[roomMatrix[row][cell]][0]==0){
                    roomInfoMatrix[roomMatrix[row][cell]][3]=row;
                    roomInfoMatrix[roomMatrix[row][cell]][4]=cell;
                }
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

        //transfer to roomInformation
        for (int room = 0; room < roomCount; room++) {
            roomInfoMatrix[room][1] = roomWidth[room][0];
            roomInfoMatrix[room][2] = (roomHeight[room][0] / roomWidth[room][0]);
        }

        //Calculate max Blacks in room based on blackCountMatrix
        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if (blackCountMatrix[row][cell] != null) {

                    if (blackCountMatrix[row][cell] == a) {
                        roomInfoMatrix[roomMatrix[row][cell]][0] = a;
                    } else {
                        roomInfoMatrix[roomMatrix[row][cell]][0] = (blackCountMatrix[row][cell]);
                    }

                }
            }
        }

        System.out.println(Arrays.deepToString(roomInfoMatrix));

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
