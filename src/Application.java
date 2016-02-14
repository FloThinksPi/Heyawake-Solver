import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by flo on 10.02.16.
 */

public class Application {

    final static int a = Config.instance.a;

    public static void main(String args[]) {

        TestData testData = new TestData();

        final int[][] blackMatrix = testData.getBlackMatrix();
        final int[][] roomInfoMatrix = testData.getRoomInfoMatrix();
        final int[][] roomMatrix = testData.getRoomMatrix();

        int[][][][] roomPossibilityMatrix = calculateAllPossibilitysForRooms(roomInfoMatrix, roomMatrix);


        int[][] resultMatrix = new int[blackMatrix.length][blackMatrix[0].length];

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                resultMatrix[row][cell] = roomPossibilityMatrix[roomMatrix[row][cell]][0][row][cell];
            }
        }

        System.out.println("\nFINAL Solution for Room ");
        for (int[] ints : resultMatrix) {
            System.out.println(Arrays.toString(ints));
        }
        System.out.println("\n");

    }

    //Calcs all possibillitys a rom can have(CalcValidRooms Wrapper) Result is a Multi Demensional Array.
    //Result = [RoomIndex][SolutionIndex][Row][Cells] -> gives back all Possibiliitys a all rooms can have for themselves (without connection to other rooms, viewed as single hayawake)
    //Sort of Divide and Conquer
    public static int[][][][] calculateAllPossibilitysForRooms(int[][] roomInfoMatrix, int[][] roomMatrix) {

        //[RoomNumber][Possibility][Width][Height]
        int[][][][] roomPossibilityMatrix = new int[roomInfoMatrix.length][][][];
        List<int[][]> possibilitys = new LinkedList<>();

        for (int roomIndex = 0; roomIndex < roomInfoMatrix.length; roomIndex++) {

            boolean exact;
            int blackCount;

            if (roomInfoMatrix[roomIndex][0] != a) {
                exact = true;
                blackCount = roomInfoMatrix[roomIndex][0];
            } else {
                exact = false;
                blackCount = roomInfoMatrix[roomIndex][0] = (int) Math.ceil((roomInfoMatrix[roomIndex][1] * roomInfoMatrix[roomIndex][2]) / 2.0);
            }

            if (exact) {

                possibilitys = calculateValidRooms(roomInfoMatrix, roomMatrix, roomIndex, blackCount, 0, 0);

//                for (int[][] ints : calculateValidRooms(roomInfoMatrix, roomMatrix, roomIndex, blackCount, 0, 0)) {
//                    System.out.println("New Solution for Room " + roomIndex);
//                    for (int[] anInt : ints) {
//                        System.out.println(Arrays.toString(anInt));
//                    }
//                }
//                System.out.println("\n");

            } else {

                for (int i = 0; i <= blackCount; i++) {
                    possibilitys.addAll(calculateValidRooms(roomInfoMatrix, roomMatrix, roomIndex, i, 0, 0));
                }

            }

            roomPossibilityMatrix[roomIndex] = new int[possibilitys.size()][][];

            int count = 0;
            for (int[][] possibility : possibilitys) {
                roomPossibilityMatrix[roomIndex][count] = possibility;
                count++;
            }

            possibilitys.clear();

        }

        return roomPossibilityMatrix;
    }

    //Calcs all Combinations a room can have given a fixed number of black fields it should have.
    public static ArrayList<int[][]> calculateValidRooms(int[][] roomInfoMatrix, int[][] roomMatrix, int roomIndex, int exactNumBlacks, int itterationRow, int itterationCell) {

        ArrayList<int[][]> possibilityResults = new ArrayList<>();
        int[][] tempRoom = new int[roomInfoMatrix[roomIndex][2]][roomInfoMatrix[roomIndex][1]];
        int gesetzt = 0;

        int startCell = itterationCell;

        if (exactNumBlacks == 0) {
            possibilityResults.add(copyMatrixToFullSize(tempRoom, roomMatrix, roomIndex));
        }

        for (int row = itterationRow; row < roomInfoMatrix[roomIndex][2]; row++) {
            for (int cell = startCell; cell < roomInfoMatrix[roomIndex][1]; cell++) {

                if (checkBlackNeighbours(tempRoom, row, cell)) {
                    tempRoom[row][cell] = 1;
                    gesetzt++;
                }
                if (gesetzt == exactNumBlacks) {
                    possibilityResults.add(copyMatrixToFullSize(tempRoom, roomMatrix, roomIndex));
                    tempRoom[row][cell] = 0;
                    gesetzt--;
                }
            }
            startCell = 0;
        }

        if (exactNumBlacks > 1) {
            if (itterationCell + 1 < roomInfoMatrix[roomIndex][1]) {
                possibilityResults.addAll(calculateValidRooms(roomInfoMatrix, roomMatrix, roomIndex, exactNumBlacks, itterationRow, itterationCell + 1));
            } else if (itterationRow + 1 < roomInfoMatrix[roomIndex][2]) {
                possibilityResults.addAll(calculateValidRooms(roomInfoMatrix, roomMatrix, roomIndex, exactNumBlacks, itterationRow + 1, 0));
            }
        }

        //TODO Erweitere Rooms auf volle Rätsel größe

        return possibilityResults;
    }

    public static int[][] solve(int[][] blackMatrix, int[] countableRoomMatrix, int[][] blackCountMatrix, int[][] roomMatrix, int stepRow, int stepCell) {


        //TODO Try out all Permutations of Rooms


        return blackMatrix;
    }

    //Low CPU Time (9 Compares Worscase=Commen Use)
    public static boolean checkBlackNeighbours(int[][] blackMatrix, int row, int cell) {

        if (blackMatrix[row][cell] == 1) return false;

        if (row != 0) {
            if (blackMatrix[row - 1][cell] == 1) return false;
        }

        if (row != blackMatrix.length - 1) {
            if (blackMatrix[row + 1][cell] == 1) return false;
        }

        if (cell != 0) {
            if (blackMatrix[row][cell - 1] == 1) return false;
        }

        if (cell != blackMatrix[0].length - 1) {
            if (blackMatrix[row][cell + 1] == 1) return false;
        }

        return true;
    }


    //High CPU Time(Guessed)
    //Kann erst am ende bestimmt werden !
    public static boolean checkWhiteLines(int[][] blackMatrix, int[][] roomMatrix, int row, int cell) {
        //TODO Write Check for Straight lines
        return false;
    }

    //Low CPU Time(Guessed)
    public static boolean checkWhiteReachable(int[][] blackMatrix, int row, int cell) {
        //TODO Write Check for Connected Whites
        return false;
    }

    //Copys Array and inserts it in the right place in a Full sized(sized like complete heyawake) array.
    public static int[][] copyMatrixToFullSize(int[][] aRoom, int[][] roomMatrix, int roomIndex) {

        int[][] newMatrix = new int[roomMatrix.length][roomMatrix[0].length];

        int startRow = -1;
        int startCell = -1;
        for (int row = 0; row < newMatrix.length; row++) {
            for (int cell = 0; cell < newMatrix[0].length; cell++) {
                if (roomMatrix[row][cell] == roomIndex) {
                    if (startRow == -1) {
                        startRow = row;
                        startCell = cell;
                    }
                    if (startRow != -1) newMatrix[row][cell] = aRoom[row - startRow][cell - startCell];
                }
            }
        }

        return newMatrix;
    }

}
