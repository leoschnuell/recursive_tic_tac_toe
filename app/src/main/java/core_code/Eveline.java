package core_code;

import static java.lang.Math.max;

import com.example.picture_button.Board;

import java.util.Map;
import java.util.Random;

public class Eveline implements Player {
    GameController gameController;
    private int[] gameboard;
    boolean[] isChecked = new boolean[9];
    private int[] possiField = new int[9];
    private int last;
    private int[] scoreboard = new int[100];
    int[] field = new int[9];

    int isPlayer;
    int isNotPlayer;
    private Board board;

    public Eveline() {
        gameController = GameController.getGameControler();
        gameboard = gameController.getBoard();

    }

    public int move(int lastMove) {
        int tracks = 0;
        last = lastMove;
        int newMove = -1;
        int score;
        int field = lastMove % 10 * 10;// ausrechnen desnächsten Möglichen Kastens
        if (checkWin(field)) //Falls der bestimmte Kasten gewonnen ist. Rausfinden, welche Kasten als nächstes Belegt werden sollen
        {
            for (int i = 0; i <= 8; i++) {//Zurücksetzen der gecheckten und möglichen Felder
                isChecked[i] = false;
                possiField[i] = 0;
            }
            possiField = chooseField(field); //rausfinden, welche Kasten in Frage für den Move kommen
            field = selectField(); //neuen Kasten bestimmen
        }
        int bestscore = 100;
        for (int i = 1; i <= 9; i++) {
            if (gameboard[field + i] == 0) {
                gameboard[field + i] = isPlayer;
                score = minimax(gameboard, 6, 100, -100, false); //aufrufen von Minimax ausgabe ist die Punktanzahl des Boards nach 6 zügen
                gameboard[field + i] = 0;
                if (bestscore < score) {
                    bestscore = score;
                    newMove = field + i;//Ersetzung des Moves mit einem mit einem besseren Score
                }
            }
        }
        return newMove; //Ausgabe des Moves
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;

        if (board.getP1() == Board.playerType.EVELINE) { //Überprüft welcher Player die AI ist
            isPlayer = 3;
            isNotPlayer = 5;
        } else {
            isNotPlayer = 3;
            isPlayer = 5;
        }

    }

    public boolean checkWin(int field) { // überprüft, ob ein Feld bereits gewonnen ist.
        return gameboard[field * 10] != 0;
    }

    public String has_won() {
        return null;
    }

    int tracker;
    int[] tracks = new int[7];

    int minimax(int[] gameboard, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 6) {
            tracks[0] = last % 10;
        }
        if (depth == 0) { //checkt bis zu einer Tiefe (vorangehende Moves) bis 6 sonst zu viele Mögliche Sachen
            for (int i = 0; i < 6; i++) {
                tracks[i] = 0;
            }
            return evaluate(); // gibt die Punktzahl des Bretts zurück
        }
        if (isMax) { // es ist die ai in dem Zug dran
            int field = tracks[6 - depth] % 10 * 10; // ausgeben des nächsten Kastens
            int bestscore = -100;
            for (int i = 0; i < 9; i++) {
                int score = -100;
                if (gameboard[field + i] == 0) {// freies Feld in dem Kasten
                    gameboard[field + i] = isPlayer; //setzt den Kästchen als ob die AI ihn gespielt hätte
                    tracks[6 - depth + 1] = i;// speichert die nächsten Kasten ab
                    score = minimax(gameboard, depth - 1, alpha, beta, false); //schaut in einen nächsten Zug
                    gameboard[field + i] = 0; // setzt das Brett auf seinen Ausgangszustand zurück
                    if (score > bestscore) {
                        bestscore = score;
                    }
                    alpha = max(score, alpha);
                    if (alpha <= beta) {
                        break;
                    }
                }
            }
            return bestscore;//Rückgabe des Werts des Spielfelds
        } else {// es ist die ai in dem Zug dran
            int field = tracks[6 - depth] % 10 * 10;// ausgeben des nächsten Kastens
            int bestscore = 100;
            for (int i = 0; i < 9; i++) {
                int scor = 100;
                if (gameboard[field + i] == 0) { // freies Feld in dem Kasten
                    gameboard[field + i] = isNotPlayer; //setzt den Kästchen als ob der Gegner ihn gespielt hätte
                    tracks[6 - depth + 1] = i; // speichert die nächsten Kasten ab
                    scor = minimax(gameboard, depth - 1, alpha, beta, true);
                    gameboard[field + i] = 0;// setzt das Brett auf seinen Ausgangszustand zurück
                    if (scor > bestscore) {
                        bestscore = scor;
                    }
                    beta = Math.min(scor, beta);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestscore; //Rückgabe des Werts des Spielfelds
        }
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
    private int evaluate() { //bewertet ein Feld, mit positiven und negativen Werte, je weiter der Wert in die Positive Richtung fällt, desto besser ist es für die AI,
        //JE niedriger der Wert ist, desto besser für den Gegenspieler
        for (int field = 0; field < 100; field = field + 10) {
            //zwei in einer Reihe +1
            //der Kasten in der Mitte hat eine höhere Bewertuung
            //drei in einer Reihe +5
            //zwei des Gegners Blocken -2
            if (gameboard[field + 5] == 0) {
                scoreboard[field + 5] = scoreboard[field + 5] + 1;
            }
            if (gameboard[field + 1] == isPlayer) {
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 4] = scoreboard[field + 4] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }

            }
            if (gameboard[field + 2] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] + 5;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 8] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }
            }
            if (gameboard[field + 3] == isPlayer) {
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }
            }
            if (gameboard[field + 4] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 6] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 1] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
            }
            if (gameboard[field + 5] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] + 5;
                    }
                }
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                    if (gameboard[field + 8] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] + 5;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                    if (gameboard[field + 6] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                    if (gameboard[field + 4] == isPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] + 5;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                    if (gameboard[field + 2] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 1] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 6] == 3) {
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 7] == isPlayer) {
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 8] = scoreboard[field + 8] + 5;
                    }
                }
            }
            if (gameboard[field + 8] == isPlayer) {
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 9] == isPlayer) {
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                }
            }
            if (gameboard[field + 1] == isNotPlayer) {
                if (gameboard[field + 2] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 4] = scoreboard[field + 4] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }

            }
            if (gameboard[field + 2] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 8] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }
            }
            if (gameboard[field + 3] == isNotPlayer) {
                if (gameboard[field + 6] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }
            }
            if (gameboard[field + 4] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 6] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 1] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
            }
            if (gameboard[field + 5] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] - 2;
                    }
                }
                if (gameboard[field + 2] == 0) {
                    if (gameboard[field + 8] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] - 2;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    if (gameboard[field + 6] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 6] == 0) {
                    if (gameboard[field + 4] == isNotPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] - 2;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 8] == 0) {
                    if (gameboard[field + 2] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    if (gameboard[field + 1] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
            if (gameboard[field + 6] == isNotPlayer) {
                if (gameboard[field + 3] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
            if (gameboard[field + 7] == isNotPlayer) {
                if (gameboard[field + 8] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 8] = scoreboard[field + 8] - 2;
                    }
                }
            }
            if (gameboard[field + 8] == isNotPlayer) {
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
        }
        int fieldscore = 0; //Zusammenzählung der Gesamtpunkte des Bretts
        for(int x = 10; x <= 90; x = x+10) {
            for (int i = 1; i <= 9; i++) {
                fieldscore = fieldscore + scoreboard[x + i];

            }
        }
        return fieldscore;
    }

}
