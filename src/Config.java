/**
 * Created by flo on 10.02.16.
 */
public enum Config {
    instance;

    final Integer a = -1; //RoomValue for Any
    final Integer i = null; //Ignored

    //http://www.janko.at/Raetsel/Heyawake/331.a.htm
    final int[][] R_331 = {
            {0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 5, 6, 7, 7, 7},
            {3, 3, 3, 5, 8, 8, 8, 8},
            {3, 3, 3, 5, 9, 9, 9, 10},
            {4, 4, 4, 4, 9, 9, 9, 10}
    };
    final Integer[][] B_331 = {
            {i, 2, i, i, i, i, i, a},
            {i, i, i, i, i, i, i, i},
            {i, i, a, a, a, i, i, a},
            {i, i, a, i, i, i, i, a},
            {i, i, i, i, i, i, i, a},
            {i, i, i, a, i, i, 3, i}
    };

    //Todo Add more Tests
    //http://www.janko.at/Raetsel/Heyawake/459.a.htm
    final int[][] R_459 = {
            {0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 5, 6, 7, 7, 7},
            {3, 3, 3, 5, 8, 8, 8, 8},
            {3, 3, 3, 5, 9, 9, 9, 10},
            {4, 4, 4, 4, 9, 9, 9, 10}
    };
    final Integer[][] B_459 = {
            {i, 2, i, i, i, i, i, a},
            {i, i, i, i, i, i, i, i},
            {i, i, a, a, a, i, i, a},
            {i, i, a, i, i, i, i, a},
            {i, i, i, i, i, i, i, a},
            {i, i, i, a, i, i, 3, i}
    };


    //Define RoomMatrix and BlackCountMatrix for calculation Here!
    final Integer[][] activeBlackCountMatrix=B_331;
    final int[][] activeRoomMatrix=R_331;

}
