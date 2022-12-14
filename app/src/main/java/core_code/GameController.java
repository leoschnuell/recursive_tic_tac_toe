package core_code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameController {


    public static int[] gamebord = new int[100];
    private static final Map<Integer, int[]> neighbours = new HashMap<>();
    private static final Map<Integer, int[]> thirdCase = new HashMap<>();
    private ArrayList<Integer> moveList = new ArrayList<>();
    private int moveCounter;
    public int lastMove;
    private static GameController gameController = new GameController();
    private boolean first_player_turn = true;
    private int move;

    GameController() {
        for (int i = 0; i < 100; i++) {
            gamebord[i] = 0;
        }
        lastMove = 1;// 1 = beginning of game

        neighbours.put(1, new int[]{2, 4});
        neighbours.put(2, new int[]{1, 3, 5});
        neighbours.put(3, new int[]{2, 6});
        neighbours.put(4, new int[]{1, 5, 7});
        neighbours.put(5, new int[]{2, 4, 6, 8});
        neighbours.put(6, new int[]{3, 5, 9});
        neighbours.put(7, new int[]{4, 8});
        neighbours.put(8, new int[]{5, 7, 9});
        neighbours.put(9, new int[]{6, 8});

        thirdCase.put(1, new int[]{3, 7});
        thirdCase.put(2, new int[]{8});
        thirdCase.put(3, new int[]{1, 9});
        thirdCase.put(4, new int[]{6});
        thirdCase.put(5, new int[]{});
        thirdCase.put(6, new int[]{4});
        thirdCase.put(7, new int[]{1, 9});
        thirdCase.put(8, new int[]{2});
        thirdCase.put(9, new int[]{3, 7});

        gameController = this;

    }

    public static GameController getGameControler() {
        return gameController;
    }

    public static Map<Integer, int[]> getNeighbours() {
        return neighbours;
    }

    public void add_move(int move) {
        moveList.add(move);
        moveCounter ++;
    }

    public Player game_setup( Player p1, Player p2) {
        //ini game
        //Player p1 = new leo_alg();  //interal int = 3
        //Player p2 = new UnitTester();  //interal int = 5
        p1.is_beginning(true);
        p2.is_beginning(false);
        Player win = gameController.gameLoop(p1, p2);
        System.out.println("end of game winner: " + (win == p1 ? "p1" : p2));

        System.out.println(win.has_won());

        for (int j = 0; j < 2; j++) {
            System.out.print("\nPlayer " + (j + 1) + " moves : \n(");
            for (int i = 0; i < gameController.moveList.size(); i++) {
                if (i % 2 == j)
                    System.out.print(gameController.moveList.get(i) + ",");
            }
            System.out.print(")");
        }
        return win;
    }

    public Player gameLoop(Player p1, Player p2) {
        // returns the winner

        while (true) {
            if (first_player_turn) {
                move = p1.move(lastMove);
                System.out.println("S1: " + move);
            } else {
                move = p2.move(lastMove);
                System.out.println("S2: " + move);

            }
            if (move == 100 || !checkMove(move)) {
                System.out.println("move:" + move + " was invalid");
                return !first_player_turn ? p1 : p2;
            }
            gamebord[move] = first_player_turn ? 3 : 5;
            moveList.add(move);
            moveCounter++;
            if (0 != checkWin(move))
                return first_player_turn ? p1 : p2;
            lastMove = move;
            first_player_turn = !first_player_turn;
        }
    }


    public int[] getBoard() {
        return gamebord;
    }

    /*
    This funktions prints the game in a grid like this
    11 12 13 21 22 23 31 32 33
    */
    @Deprecated
    public void display() {
        for (int j = 1; j < 4; j++) {
            for (int i = 10; i < 40; i += 10) {
                System.out.print((gamebord[i*j] == 0 ? "_" : gamebord[i*j] == 3 ? "X" : "O") + (i != 30 ? "|" : ""));
            }
            System.out.println();
        }
        System.out.println("=====================");
        for (int G = 0; G < 3; G++) {
            for (int K = 0; K < 3; K++) {
                for (int g = 1; g < 4; g++) {
                    for (int k = 1; k < 4; k++) {
                        int id = (G * 3 + g) * 10 + K * 3 + k;
                        //System.out.print( id+ "  ");
                        System.out.print((gamebord[id] == 0 ? "_" : gamebord[id] == 3 ? "X" : "O") + " ");
                    }
                    System.out.print(g != 3 ? "|" : "");
                }
                System.out.println();
            }
            System.out.print(G != 2 ? "------|------|-----\n" : "\n");

        }
    }


    public boolean checkMove(int playerMove) {
        if (lastMove == 1) // first move of the game
            return true;
        int soll_kasten = (lastMove % 10);
        int ist_kasten = playerMove / 10;
        if (!(playerMove > 10 && playerMove < 100 && playerMove % 10 != 0))
            return false;//unmögliche zahlen ausschliesen
        if (gamebord[playerMove] != 0)
            return false;// is das gewählt kästchen unbesetzt
        if (gamebord[soll_kasten * 10] == 0) {// 1 fall: der gewähle kasten ist offen
            return (soll_kasten == ist_kasten);//prüft ob der kasten gleich dem letzten kästchen ist

        } else {//2 fall: gewählert kasten ist voll
            boolean es_gibt_einen = false;//eine flag
            for (int nachbar_soll : neighbours.get(soll_kasten)) {//für jeder nachbar vom letzen move
                if (gamebord[nachbar_soll * 10] == 0) {// nachbar ist noch nicht blockirt
                    es_gibt_einen = true;
                    if (ist_kasten == nachbar_soll) {//gewählter kasten ist gleich einem  nachbaren
                        return true;
                    }
                }
            }
            if (es_gibt_einen) {//keiner der nachbaren ist frei
                return false;
            } else {
                // 3 fall: prüfe ob es einen freien axen nachbar gibt
                for (int i : thirdCase.get(soll_kasten)) {//im third case sind nur indirekte nachbarn drinne (es wird nicht doppel geprüft )
                    if (gamebord[i * 10] == 0) {// einer der axen ist lehr
                        es_gibt_einen = true;
                        if (ist_kasten == i) {//spieler hat in den kasten plaziert
                            return true;
                        }
                    }
                }
                if (es_gibt_einen) {//es_git einenen freihen axen nachbarn aber er wurde nicht gewählt
                    return false;
                }else{
                    // wenn es keine freie axie gibt darf der spieler überall hingehen wo ein kasten nicht voll ist
                    return gamebord[ist_kasten*10] ==0;
                }
            }
        }
    }
//Returns values -3,-5 , 0 - 9
    //0 = nothing was won
    // -3 first player won the game
    // -5 second player won the game
    // 1..9 kasten was won
    public int checkWin(int playerMove) {
        int res = checkKasten(playerMove / 10);
        if (res > 0) {
            System.out.println("kasten gewonnen:"+ (playerMove/10)*10);
            gamebord[(playerMove / 10) * 10] = res;
            // check flags of other kasten
            int[] result = new int[8];
            // code can be reduced is doppelt
            result[0] = gb(10) + gb(20) + gb(30);
            result[1] = gb(40) + gb(50) + gb(60);
            result[2] = gb(70) + gb(80) + gb(90);
            result[3] = gb(10) + gb(40) + gb(70);
            result[4] = gb(20) + gb(50) + gb(80);
            result[5] = gb(30) + gb(60) + gb(90);
            result[6] = gb(10) + gb(50) + gb(90);
            result[7] = gb(30) + gb(50) + gb(70);

            for (int i = 0; i < 8; i++) {
                if (result[i] == 9)
                    return -3;
                else if (result[i] == 15)
                    return -5;
            }
            return playerMove / 10;
        }
        return 0;
    }

    private int checkKasten(int k) {// k = kasten // returns true if kasten is won
    /*
    123
    456
    789
    147
    258
    369
    159
    357
 */
        k *= 10;
        int[] result = new int[8];
        result[0] = gb(k + 1) + gb(k + 2) + gb(k + 3);
        result[1] = gb(k + 4) + gb(k + 5) + gb(k + 6);
        result[2] = gb(k + 7) + gb(k + 8) + gb(k + 9);
        result[3] = gb(k + 1) + gb(k + 4) + gb(k + 7);
        result[4] = gb(k + 2) + gb(k + 5) + gb(k + 8);
        result[5] = gb(k + 3) + gb(k + 6) + gb(k + 9);
        result[6] = gb(k + 1) + gb(k + 5) + gb(k + 9);
        result[7] = gb(k + 3) + gb(k + 5) + gb(k + 7);
        for (int i = 0; i < 8; i++) {
            if (result[i] == 9) {
                return 3;
            }
            else if (result[i] == 15) {
                return 5;
            }
        }
        return 0;
    }

    private int gb(int i) {
        return gamebord[i];
    }


}
