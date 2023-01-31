package core_code;

import com.example.picture_button.Board;

public class oloi implements Player {
    int isPlayer;
    int isNotPlayer;
    GameController gameControler;
    private int[] gamebord;
    private Board board;

    public oloi() {
        gameControler = GameController.getGameControler();
        gamebord = gameControler.getBoard();
        if (board.getP1() == Board.playerType.OLOI) {//Überprüft welcher Player die AI ist
            isPlayer = 3;
            isNotPlayer = 5;
        } else {
            isNotPlayer = 3;
            isPlayer = 5;
        }
    }

    @Override
    public void setBoard(Board board) {
this.board = board;
    }

    @Override
    public int move(int lastMove) {
        int move = -1;
        int bestresult = -100;
        int field = lastMove % 10 * 10;//Wählen des nächsten Feldes
        for (int i = 1; i < 10; i++) {//durchgehen des Feldes
            if (gamebord[field] == 0) {//wenn ein Feld Frei ist ausführen
                gamebord[field + i] = 5;// Setzen eines möglichen Zuges
                int result = minimax(gamebord, 0, false, lastMove);
                gamebord[field + i] = 0;// Rurücksetzen eines möglichen Zuges
                if (result > bestresult) {//erneuerung des besten Zuges
                    bestresult = result;
                    move = field + i;
                }
            }

        }
        return move;//Ausgabe des Zuges
    }


    int[] score = {-1, 0, 1};

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
}
