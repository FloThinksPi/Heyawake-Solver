import java.util.Arrays;

/**
 * Created by flo on 10.02.16.
 */

public class Application {

    public static void main(String args[]) {

        TestData testData = new TestData();

        final int[][] blackMatrix = testData.getBlackMatrix();
        final int[][] roomInfoMatrix = testData.getRoomInfoMatrix();
        final int[][] roomMatrix = testData.getRoomMatrix();
        final int[][] blackCountMatrix = testData.getBlackCountMatrix();

    }

    public static int[][][][] calculateAllPossibilitysForRoom(int[][] roomInfoMatrix){

        //[RoomNumber][Possibility][Width][Height]
        int[][][][] roomPossibilityMatrix=new int[roomInfoMatrix.length][][][];

        for(int roomIndex=0;roomIndex<roomInfoMatrix.length;roomIndex++){

        }

        return roomPossibilityMatrix;
    }

    public static int[][] solve(int[][] blackMatrix, int[] countableRoomMatrix, int[][] blackCountMatrix, int[][] roomMatrix,int stepRow,int stepCell) {

        int oldStepCell=stepCell;
        int oldStepRow=stepRow;

        blackMatrix[stepRow][stepCell] = 1;

        //Vorausschauend schritte machen
        if (checkBlackNeighbours(blackMatrix, stepRow, stepCell) && checkBlackCountInRoom(blackMatrix, countableRoomMatrix, blackCountMatrix, roomMatrix,false)) {

            if(stepCell+2>=blackMatrix[0].length){
                stepCell=(blackMatrix[0].length-1)-stepCell;
                if(stepRow+1>=blackMatrix.length)stepRow=0;
            }else{
                stepCell+=2;
            }

            blackMatrix=solve(blackMatrix,countableRoomMatrix,blackCountMatrix,roomMatrix,stepRow,stepCell);

        }else{
            blackMatrix[stepRow][stepCell] = 0;
        }

        if(checkBlackCountInRoom(blackMatrix, countableRoomMatrix, blackCountMatrix, roomMatrix,true)){
            blackMatrix[oldStepRow][oldStepCell] = 0;
        }

        return blackMatrix;
    }

    //Low CPU Time
    public static boolean checkBlackNeighbours(int[][] blackMatrix, int row, int cell) {

        if (row != 0) {
            if (blackMatrix[row - 1][cell] == 1) return false;
        }

        if (row != blackMatrix.length - 1) {
            if (blackMatrix[row + 1][cell] == 1) return false;
        }

        if (cell != 0) {
            if (blackMatrix[row][cell - 1] == 1) return false;
        }

        if (cell != blackMatrix.length - 1) {
            if (blackMatrix[row][cell + 1] == 1) return false;
        }

        return true;
    }

    //Middle CPU Time
    //Keine Blacks in 0 Felder , Nicht zu viele Blacks -> Genau
    public static boolean checkBlackCountInRoom(int[][] blackMatrix, int[] countableRoomMatrix, int[][] blackCountMatrix, int[][] roomMatrix,boolean exact) {

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if(countableRoomMatrix[row]!=0)
                    countableRoomMatrix[roomMatrix[row][cell]] -= (blackMatrix[row][cell]);
            }
        }

        System.out.println("CountableRoomMatrix: " + Arrays.toString(countableRoomMatrix));

        for (int value : countableRoomMatrix) {
            if(exact) {
                if (value != 0) return false;
            }else{
                if (value > 0) return false;
            }
        }

        return true;
    }

    //High CPU Time
    //Kann erst am ende bestimmt werden !
    public static boolean checkWhiteLines(int[][] blackMatrix, int[][] roomMatrix, int row, int cell) {
        return false;
    }

    //Verry High CPU Time
    public static boolean checkWhiteReachable(int[][] blackMatrix, int row, int cell) {
        return false;
    }


}
