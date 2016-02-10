import java.util.Arrays;

/**
 * Created by flo on 10.02.16.
 */

public class Application {

    public static void main(String args[]) {

        TestData testData = new TestData();

        final int[][] blackMatrix = testData.getBlackMatrix();
        final int[] countableRoomMatrix = testData.getCountableRoomMatrix();
        final int[][] roomMatrix = testData.getRoomMatrix();
        final int[][] blackCountMatrix = testData.getBlackCountMatrix();

        int[][] resultMatrix=solve(blackMatrix,countableRoomMatrix,blackCountMatrix,roomMatrix,0,0);

        for (int row = 0; row < blackMatrix.length; row++) {
            System.out.println(Arrays.toString(resultMatrix[row]));
        }

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
                countableRoomMatrix[roomMatrix[row][cell]] += (blackMatrix[row][cell] - blackCountMatrix[row][cell]);
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
    public static boolean checkWhiteLines(int[][] blackMatrix, int[][] roomMatrix, int row, int cell) {
        return false;
    }

    //Verry High CPU Time
    public static boolean checkWhiteReachable(int[][] blackMatrix, int row, int cell) {
        return false;
    }


}