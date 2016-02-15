import java.util.*;

public class Application {

    final static int a = Config.instance.i;//Ignored/Any

    public static void main(String args[]) {

        System.out.println("Preparing Heyawake Data.");

        long startTime = System.nanoTime();

        DataPreparator testData = new DataPreparator();

        final int[][] roomInfoMatrix = testData.getRoomInfoMatrix();
        final int[][] roomMatrix = testData.getRoomMatrix();

        int[][][][] roomPossibilityMatrix = calculateAllPossibilitysForRooms(roomInfoMatrix, roomMatrix);


//        System.out.println("Sorting");
//        Arrays.sort(roomPossibilityMatrix, (s1, s2) -> {
//            if (s1.length < s2.length)
//                return 1;    // tells Arrays.sort() that s1 comes after s2
//            else if (s1.length > s2.length)
//                return -1;   // tells Arrays.sort() that s1 comes before s2
//            else {
//                return 0;
//            }
//        });

        long stopTime = System.nanoTime();
        System.out.println("Prepared Heyawake Data in " + (stopTime - startTime) / 1000000000.0 + " Seconds");
        System.out.println("\n Calculating Solution Now");

        startTime = System.nanoTime();

        int[][] resultMatrix = new int[roomMatrix.length][roomMatrix[0].length];
        resultMatrix = findResult(roomPossibilityMatrix, roomMatrix, roomInfoMatrix, resultMatrix, 0);

        stopTime = System.nanoTime();

        if (resultMatrix != null) {
            System.out.println("\n Solution found in " + (stopTime - startTime) / 1000000000.0 + " Seconds");
            for (int[] ints : resultMatrix) {
                System.out.println(Arrays.toString(ints));
            }
            System.out.println("\n");
        } else {
            System.out.println("No Solution found! in" + (stopTime - startTime) / 1000000000.0 + " Seconds");
        }


    }

    //Calcs all possibillitys a rom can have(CalcValidRooms Wrapper) Result is a (Big)Multi Demensional Array.
    //Result = [RoomIndex][SolutionIndex][Row][Cells] -> gives back all Possibiliitys a all rooms can have for themselves (without connection to other rooms, viewed as single hayawake)
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

                possibilitys = calculateCombinationOfRoomFixedBlack(roomInfoMatrix, roomMatrix, roomIndex, blackCount, 0, 0);

//                for (int[][] ints : calculateCombinationOfRoomFixedBlack(roomInfoMatrix, roomMatrix, roomIndex, blackCount, 0, 0)) {
//                    System.out.println("New Solution for Room " + roomIndex);
//                    for (int[] anInt : ints) {
//                        System.out.println(Arrays.toString(anInt));
//                    }
//                }
//                System.out.println("\n");

            } else {

                for (int i = 0; i <= blackCount; i++) {
                    possibilitys.addAll(calculateCombinationOfRoomFixedBlack(roomInfoMatrix, roomMatrix, roomIndex, i, 0, 0));
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
    public static ArrayList<int[][]> calculateCombinationOfRoomFixedBlack(int[][] roomInfoMatrix, int[][] roomMatrix, int roomIndex, int exactNumBlacks, int itterationRow, int itterationCell) {

        ArrayList<int[][]> possibilityResults = new ArrayList<>();
        int[][] tempRoom = new int[roomInfoMatrix[roomIndex][2]][roomInfoMatrix[roomIndex][1]];
        int gesetzt = 0;

        int startCell = itterationCell;

        if (exactNumBlacks == 0) {
            possibilityResults.add(copyMatrixToFullSize(tempRoom, roomMatrix, roomIndex));
        }

        for (int row = itterationRow; row < roomInfoMatrix[roomIndex][2]; row++) {
            for (int cell = startCell; cell < roomInfoMatrix[roomIndex][1]; cell++) {

                if (checkBlackNeighbours(tempRoom, row, cell, true)) {
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
                possibilityResults.addAll(calculateCombinationOfRoomFixedBlack(roomInfoMatrix, roomMatrix, roomIndex, exactNumBlacks, itterationRow, itterationCell + 1));
            } else if (itterationRow + 1 < roomInfoMatrix[roomIndex][2]) {
                possibilityResults.addAll(calculateCombinationOfRoomFixedBlack(roomInfoMatrix, roomMatrix, roomIndex, exactNumBlacks, itterationRow + 1, 0));
            }
        }

        return possibilityResults;
    }

    //Recursive Algorythm to Insert all Combination of Valid Rooms and check if the Resulting solution is Valid
    public static int[][] findResult(int[][][][] roomPossibilityMatrix, int[][] roomMatrix, int[][] roomInfoMatrix, int[][] resultMatrix, int startRoomIndex) {

        int[][] tempResultMatrix;
        boolean checkNeigbours;

        for (int possibility = 0; possibility < roomPossibilityMatrix[startRoomIndex].length; possibility++) {
            checkNeigbours = true;
            tempResultMatrix = copyMatrix(resultMatrix);

            for (int row = roomInfoMatrix[startRoomIndex][3]; row < roomMatrix.length; row++) {
                for (int cell = roomInfoMatrix[startRoomIndex][4]; cell < roomMatrix[0].length; cell++) {
                    if (roomMatrix[row][cell] == startRoomIndex) {
                        tempResultMatrix[row][cell] = roomPossibilityMatrix[startRoomIndex][possibility][row][cell];
                        if (tempResultMatrix[row][cell] == 1 && !checkBlackNeighbours(tempResultMatrix, row, cell, false)) {
                            checkNeigbours = false;
                            break;
                        }
                    }
                }
                if (!checkNeigbours) break;
            }
            if (!checkNeigbours) continue;


            if (startRoomIndex != roomPossibilityMatrix.length - 1) {
                //When this Room is not last Room , insert another one.
                tempResultMatrix = findResult(roomPossibilityMatrix, roomMatrix, roomInfoMatrix, tempResultMatrix, startRoomIndex + 1);
                //If null , All matrix combinations after this one are wrong so continue iterate this one.
                //if not null , a Suluton was found so return chain back.
                if (tempResultMatrix != null) return tempResultMatrix;
            } else {
                //When last Room was Inserted check White Lines and White Connected
                if (checkWhiteLines(tempResultMatrix, roomMatrix) && checkWhiteReachable(tempResultMatrix, 0, 0)) {
                    return tempResultMatrix;
                }

            }

        }

        return null;//No Possibilitys with found

    }

    //Checks if neigbors of a cell are valid if you would set a black into it. if includeOwnField=true returns false also when there is a black on the cell you want to check
    public static boolean checkBlackNeighbours(int[][] blackMatrix, int row, int cell, boolean includeOwnField) {

        if (includeOwnField && blackMatrix[row][cell] == 1) return false;

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

    //Check if there are any White Lines going beyond 2 Rooms
    public static boolean checkWhiteLines(int[][] sourceMatrix, int[][] roomMatrix) {

        boolean isOK = true;
        int counterHorizontal;
        int[] counterVertical = new int[sourceMatrix[0].length];
        int lastRoomHorizontal;
        int[] lastRoomVertical = new int[sourceMatrix[0].length];

        //LineCheck
        for (int row = 0; row < sourceMatrix.length; row++) {
            counterHorizontal = 0;
            lastRoomHorizontal = -1;
            for (int cell = 0; cell < sourceMatrix[0].length; cell++) {

                //Horizontal
                if (lastRoomHorizontal != roomMatrix[row][cell]) {
                    counterHorizontal++;
                    lastRoomHorizontal = roomMatrix[row][cell];
                }

                if (sourceMatrix[row][cell] == 1) {
                    counterHorizontal = 0;
                    lastRoomHorizontal = -1;
                }

                //Vertical
                if (counterHorizontal >= 3) return false;

                if (lastRoomVertical[cell] != roomMatrix[row][cell]) {
                    counterVertical[cell]++;
                    lastRoomVertical[cell] = roomMatrix[row][cell];
                }

                if (sourceMatrix[row][cell] == 1) {
                    counterVertical[cell] = 0;
                    lastRoomVertical[cell] = -1;
                }

                if (counterVertical[cell] >= 3) return false;

            }
        }

        return true;
    }

    //Recursive Search for Connected Whites
    public static int[][] checkWhiteReachableRecursive(int[][] sourceMatrix, int startRow, int startCell, boolean first) {

        int[][] blackMatrix = copyMatrix(sourceMatrix);

        //Find first White
        if (first) {
            for (int row = startRow; row < blackMatrix.length; row++) {
                for (int cell = startCell; cell < blackMatrix[0].length; cell++) {
                    if (blackMatrix[row][cell] == 0) {
                        startCell = cell;
                        startRow = row;
                        first = false;
                        break;
                    }
                }
                if (!first) break;
            }
        }

        //Recursive Search next Whites
        if (blackMatrix[startRow][startCell] == 0) {
            blackMatrix[startRow][startCell] = 2;

            if (startRow != 0) {
                if (blackMatrix[startRow - 1][startCell] == 0)
                    blackMatrix = checkWhiteReachableRecursive(blackMatrix, (startRow - 1), startCell, false);
            }

            if (startRow != blackMatrix.length - 1) {
                if (blackMatrix[startRow + 1][startCell] == 0)
                    blackMatrix = checkWhiteReachableRecursive(blackMatrix, (startRow + 1), startCell, false);
            }

            if (startCell != 0) {
                if (blackMatrix[startRow][startCell - 1] == 0)
                    blackMatrix = checkWhiteReachableRecursive(blackMatrix, startRow, (startCell - 1), false);
            }

            if (startCell != blackMatrix[0].length - 1) {
                if (blackMatrix[startRow][startCell + 1] == 0)
                    blackMatrix = checkWhiteReachableRecursive(blackMatrix, startRow, (startCell + 1), false);
            }
        }


        return blackMatrix;
    }

    //Check result of Recursive search if a white was not reached
    public static boolean checkWhiteReachable(int[][] blackMatrix, int startRow, int startCell) {

        int[][] trackedMatrix = checkWhiteReachableRecursive(blackMatrix, 0, 0, true);

        //Search unreached Whites (still 0) because reached whites are set to 2
        for (int[] ints : trackedMatrix) {
            for (int anInt : ints) {
                if (anInt == 0) return false;
            }
        }

        return true;
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

    //Copys 2D matrix 1:1
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
