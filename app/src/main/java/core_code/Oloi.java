package core_code;

import com.example.picture_button.Board;

import java.util.Map;
import java.util.Random;

public class Oloi implements Player {
    int isPlayer;
    int isNotPlayer;
    GameController gameController;
    private int[] gameboard;
    private Board board;
    boolean[] isChecked = new boolean[9];
    int[] field = new int[9];
    private int[] possiField = new int[9];
    public Oloi() {
        gameController = GameController.getGameControler();
        gameboard = gameController.getBoard();

    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
        if (board.getP1() == Board.playerType.OLOI) {//Überprüft welcher Player die AI ist
            isPlayer = 3;
            isNotPlayer = 5;
        } else {
            isNotPlayer = 3;
            isPlayer = 5;
        }
    }

    @Override
    public int move(int lastMove) {
        int move = -1;
        int bestResult = -100;
        int field = lastMove % 10 * 10;//Wählen des nächsten Feldes
        if (checkWin(field)) //Falls der bestimmte Kasten gewonnen ist. Rausfinden, welche Kasten als nächstes Belegt werden sollen
        {
            for (int i = 0; i <= 8; i++) {//Zurücksetzen der gecheckten und möglichen Felder
                isChecked[i] = false;
                possiField[i] = 0;
            }
            possiField = chooseField(field); //rausfinden, welche Kasten in Frage für den Move kommen
            field = selectField(); //neuen Kasten bestimmen
        }
        for (int i = 1; i < 10; i++) {//durchgehen des Feldes
            if (gameboard[field] == 0) {//wenn ein Feld Frei ist ausführen
                gameboard[field + i] = 5;// Setzen eines möglichen Zuges
                int result = minimax(gameboard, 0, false, lastMove);
                gameboard[field + i] = 0;// Rurücksetzen eines möglichen Zuges
                if (result > bestResult) {//erneuerung des besten Zuges
                    bestResult = result;
                    move = field + i;
                }
            }

        }
        return move;//Ausgabe des Zuges
    }


    int[] score = {-1, 0, 1}; //speicherung der möglichen Ausgänge -1 gegner gewinnt, 0 unentschieden, 1 Ai gewinnt

    public int minimax(int[] board, int depth, boolean isMax, int lastMove) {
        int winner = 2;
        winner = check(board, lastMove % 10 * 10); //überprüfung ob jmd das Feld gewonnen hat
        if (winner != 2) {//Ausgabe des Scores wenn einer das Feld gewonnen hat oder ein unentschieden ist
            return score[winner + 1];
        }
        if (isMax) {
            int bestscore = -100;
            for (int i = 1; i < 10; i++) {
                if (board[lastMove % 10 * 10 + i] == 0) {// freies Feld in dem Kasten
                    board[lastMove % 10 * 10 + i] = isPlayer; //setzt den Kästchen als ob die AI ihn gespielt hätte
                    int score = minimax(board, depth, false, lastMove);//schaut in einen nächsten Zug
                    board[lastMove % 10 * 10 + i] = 0;//setzt das Brett auf seinen Ausgangszustand zurück
                    if (score > bestscore) {
                        bestscore = score;
                    }
                }
            }
            return bestscore;//gibt den besten Score aus
        } else {
            int bestscore = 100;
            for (int i = 1; i < 10; i++) {
                if (board[lastMove % 10 * 10 + i] == 0) {// freies Feld in dem Kasten
                    board[lastMove % 10 * 10 + i] = isNotPlayer; //setzt den Kästchen als ob ihn der Gegner ihn gespielt hätte
                    int score = minimax(board, depth, true, lastMove);//schaut in einen nächsten Zug
                    board[lastMove % 10 * 10 + i] = 0; //setzt das Brett auf seinen Ausgangszustand zurück
                    if (score < bestscore) {
                        bestscore = score;
                    }
                }
            }
            return bestscore; //gibt den besten Score aus
        }
    }

    private int check(int[] gb, int field) { //überprüft ob ein Feld gewonnen wird
        int[] result = new int[8];
        result[0] = gb[field + 1] + gb[field + 2] + gb[field + 3];
        result[1] = gb[field + 4] + gb[field + 5] + gb[field + 6];
        result[2] = gb[field + 7] + gb[field + 8] + gb[field + 9];
        result[3] = gb[field + 1] + gb[field + 4] + gb[field + 7];
        result[4] = gb[field + 2] + gb[field + 5] + gb[field + 8];
        result[5] = gb[field + 3] + gb[field + 6] + gb[field + 9];
        result[6] = gb[field + 1] + gb[field + 5] + gb[field + 9];
        result[7] = gb[field + 3] + gb[field + 5] + gb[field + 7];

        for (int i = 0; i < 8; i++) {
            if (isPlayer == 3) {
                if (result[i] == 9)
                    return -1;
                else if (result[i] == 15)
                    return 1;
            } else {
                if (result[i] == 15)
                    return -1;
                else if (result[i] == 9)
                    return 1;
            }
        }
        int count = 0;
        for (int i = 1; i < 10; i++) {
            if (gb[field + i] == 0) {
                count++;
            }
        }
        if (count == 0) {
            return 0;
        } else {
            return 2;
        }
    }
    public boolean checkWin(int field) { // überprüft, ob ein Feld bereits gewonnen ist.
        return gameboard[field * 10] != 0;
    }
    private int[] chooseField(int checkField) {

        //Bestimmung eines nächsten Kastens wenn das eine schon benutzt ist
        Map<Integer, int[]> possible = gameController.getneighbors(checkField);
        int count = 0;
        for (int i = 0; i < possible.get(checkField).length; i++) {
            if (!checkWin(possible.get(checkField)[i])) {
                field[count] = possible.get(checkField)[i];
                count++;
            }
        }

        count = 0;
        if (field[0] == 0) {
            Map<Integer, int[]> thirdCase = gameController.getthirdCase(checkField);
            for (int i = 0; i < thirdCase.get(checkField).length; i++) {
                if (gameboard[thirdCase.get(checkField)[i] * 10] == 0) {
                    field[count] = thirdCase.get(checkField)[i];
                    count++;
                }
            }
            count = 0;
            if (field[0] == 0) {
                for (int i = 1; i < 10; i++) {
                    if (gameboard[i * 10] == 0) {
                        field[count] = i;
                        count++;
                    }
                }
            }

        }
        return field;
    }
    private int selectField(){
        int x = 0; //sonst Bestimmung durch Zufall eines möglichen Feldes
        java.util.Random rand = new Random();
        while (possiField[x] != 0) {
            x++;
        }

        return possiField[rand.nextInt(x)];
    }
}
