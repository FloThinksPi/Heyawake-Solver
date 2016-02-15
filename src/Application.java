import java.util.*;

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

        //TODO Sort Possibilitys to get better performance

        int[][] resultMatrix = new int[roomMatrix.length][roomMatrix[0].length];
        resultMatrix = findResult(roomPossibilityMatrix, roomMatrix, resultMatrix, 0);

        if (resultMatrix != null) {
            System.out.println("\nFINAL Solution for Room ");
            for (int[] ints : resultMatrix) {
                System.out.println(Arrays.toString(ints));
            }
            System.out.println("\n");
        } else {
            System.out.println("Keine Lösung gefunden!");
        }


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

                if (checkBlackNeighbours(tempRoom, row, cell,true)) {
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

        return possibilityResults;
    }

    //Low CPU Time (9 Compares Worscase=Commen Use)
    public static boolean checkBlackNeighbours(int[][] blackMatrix, int row, int cell,boolean includeOwnField) {

        if (includeOwnField&&blackMatrix[row][cell] == 1) return false;

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

    public static int[][] findResult(int[][][][] roomPossibilityMatrix, int[][] roomMatrix, int[][] resultMatrix, int startRoomIndex)  {

        int[][] tempResultMatrix = resultMatrix;
        boolean checkNeigbours=true;
        boolean checkWhiteReachable=true;

        for (int possibility = 0; possibility < roomPossibilityMatrix[startRoomIndex].length; possibility++) {
//            System.out.println("Room: "+startRoomIndex+" Possibillity: "+possibility);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            //Todo start with OFFSET (Width and Height) -> Better Performance
            for (int row = 0; row < roomMatrix.length; row++) {
                for (int cell = 0; cell < roomMatrix[0].length; cell++) {
                    if(roomMatrix[row][cell]==startRoomIndex)tempResultMatrix[row][cell] = roomPossibilityMatrix[startRoomIndex][possibility][row][cell];
                }
            }


            if (startRoomIndex != roomPossibilityMatrix.length - 1) {
                tempResultMatrix = findResult(roomPossibilityMatrix, copyMatrix(roomMatrix), tempResultMatrix, startRoomIndex + 1);
                if (tempResultMatrix != null) return tempResultMatrix;
                else tempResultMatrix = resultMatrix;
            } else {

                for (int row = 0; row < roomMatrix.length; row++) {
                    for (int cell = 0; cell < roomMatrix[0].length; cell++) {

                        if (tempResultMatrix[row][cell]==1&&!checkBlackNeighbours(tempResultMatrix, row, cell,false)){
                            checkNeigbours=false;
                            break;
                        }

                    }
                    if(checkNeigbours==false)break;
                }
                if(checkNeigbours==false)continue;

                if (checkNeigbours&&checkWhiteReachable(tempResultMatrix, 0, 0)) {
                    return tempResultMatrix;
                } else {
                    checkWhiteReachable=false;
                }

            }

        }

        return null;

    }

    //High CPU Time(Guessed)
    //Kann erst am ende bestimmt werden !
    public static boolean checkWhiteLines(int[][] blackMatrix, int[][] roomMatrix, int row, int cell) {
        //TODO Write Check for Straight lines
        return false;
    }


    public static boolean checkWhiteReachable(int[][] blackMatrix, int startRow, int startCell) {

        int[][] trackedMatrix = checkWhiteReachableRecursive(blackMatrix, 0, 0);

//        System.out.println("\nWhite Pathfinding Matrix");
        for (int[] ints : trackedMatrix) {
            for (int anInt : ints) {
                if (anInt == 0) return false;
            }
//            System.out.println(Arrays.toString(ints));
        }
//        System.out.println("\n");

        return true;
    }

    //Low CPU Time(Guessed)
    public static int[][] checkWhiteReachableRecursive(int[][] sourceMatrix, int startRow, int startCell) {
        //TODO Write Check for Connected Whites

        int[][] blackMatrix = copyMatrix(sourceMatrix);

        for (int row = 0; row < blackMatrix.length; row++) {
            for (int cell = 0; cell < blackMatrix[0].length; cell++) {
                if (blackMatrix[row][cell] == 0) {
                    blackMatrix[row][cell] = 2;

                    if (row != 0) {
                        if (blackMatrix[row - 1][cell] == 0)
                            blackMatrix = checkWhiteReachableRecursive(blackMatrix, (row - 1), cell);
                    }

                    if (row != blackMatrix.length - 1) {
                        if (blackMatrix[row + 1][cell] == 0)
                            blackMatrix = checkWhiteReachableRecursive(blackMatrix, (row + 1), cell);
                    }

                    if (cell != 0) {
                        if (blackMatrix[row][cell - 1] == 0)
                            blackMatrix = checkWhiteReachableRecursive(blackMatrix, row, (cell - 1));
                    }

                    if (cell != blackMatrix[0].length - 1) {
                        if (blackMatrix[row][cell + 1] == 0)
                            blackMatrix = checkWhiteReachableRecursive(blackMatrix, row, (cell + 1));
                    }
                }
            }
        }

        return blackMatrix;
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

    public static int[][] copyMatrix(int[][] matrix) {
        int[][] myInt = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            int[] aMatrix = matrix[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

}
