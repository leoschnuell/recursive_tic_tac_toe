package core_code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.picture_button.Board;


public class Liz_alg implements Player {

    GameController gameController;
    private int[] gameboard;
    int whoWins = 0;
    int counter;
    int isPlayer;
    int isNotPlayer;
    private int[] lessTwoField = new int[9];  //
    private int[] freeField = new int[9]; //freie Felder in einem Kasten
    private int[] enemySafe = new int[9];
    private int[] possiField = new int[9];
    private int newField;
    private Board board;

    public Liz_alg() {
        gameController = GameController.getgameController();
        gameboard = gameController.getBoard();
        if (board.getP1() == Board.playerType.DAISY) {//Überprüft welcher Player die AI ist
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
        counter = 0;
        gameboard = gameController.getBoard();
        newField = lastMove % 10; //Bestimmung des nächsten Kastens
        if (checkWin(newField)) //Falls der bestimmte Kasten gewonnen ist. Rausfinden, welche Kasten als nächstes Belegt werden sollen
        {
            for (int i = 0; i <= 8; i++) {//Zurücksetzen der gecheckten und möglichen Felder
                isChecked[i] = false;
                possiField[i] = 0;
            }
            chooseField(newField); //rausfinden, welche Kasten in Frage für den Move kommen
            newField = selectField(); //neuen Kasten bestimmen
        }

        int x = 0;
        int p = 0;
        for (int i = 0; i < 9; i++) { //Setzten auf den Ausgangszustand
            freeField[i] = 0;
            enemySafe[i] = 0;
            lessTwoField[i] = 0;
        }
        for (int i = 1; i < 10; i++) {
            if (gameboard[newField * 10 + i] == 0) { //Welche Felder potentiell benutzt werden können
                freeField[x] = i;
                x++;
            }
        }
        x = 0;
        int y = 0;
        int b = 0;
        while (freeField[y] != 0) {
            if (justOneOrLessEnemy(freeField[y])) { // überprüft  ob nur weniger als zwei Kästchen vom Gegner besetzt sind für den näöchsten Kasten des Gegners
                lessTwoField[b] = freeField[y]; //Speichert diese ab
                if (b > 8) {
                    b++;
                }
            }
            if (y >= 8) {
                break;
            }
            y++;
        }
        if (lessTwoField[1] == 0 && lessTwoField[0] % 10 != 5) { // wenn nur eine Möglichkeit ist, dass der Gegner zwei oder weniger im nächsten Kasten hat ausgeben
            return (newField) * 10 + lessTwoField[0];
        } else if (lessTwoField[2] == 0 && lessTwoField[1] != 0) {
            if (lessTwoField[0] % 10 == 5) { // testen ob man den Gegner davon abhalten kann in dem Nächsten Schritt in die Mitte zu setzten
                return (newField) * 10 + lessTwoField[1]; //dann die Alternative wählen
            }
            if (lessTwoField[1] % 10 == 5) {//dann die Alternative nehmen
                return (newField) * 10 + lessTwoField[0];
            }
        } else if (lessTwoField[0] != 0 && lessTwoField[1] != 0) {
            Random rand = new Random(); // sonst wird es random entschieden
            int h = lessTwoField[rand.nextInt(b + 1)];
            while (h == 0) {
                h = lessTwoField[rand.nextInt(b + 1)];
            }
            return (newField) * 10 + h; //das neue Feld ausgeben
        }

        while (freeField[p] != 0 && p < 9) {
            if (!almostThreeInARow(freeField[p], isNotPlayer)) {// welche freien Felder benutzt werden könen die es dem Gegner nicht ermöglichen im nächsten Zu eine Reihe vollzubekommen
                enemySafe[x] = freeField[p];
                x++;
            }
            p++;
        }
        if (enemySafe[2] == 0) {
            if (enemySafe[0] % 10 == 5)  // falls man nur zwei möglichkeiten hat, und eine den Gegner in die Mitte gehen lässt, die andere Wählen
            {
                enemySafe[0] = enemySafe[1];
                enemySafe[1] = 0;
            }
            if (enemySafe[1] % 10 == 5) {// falls man nur zwei möglichkeiten hat, und eine den Gegner in die Mitte gehen lässt, die andere Wählen
                enemySafe[1] = 0;
            }
        }
        if (enemySafe[1] == 0) {//bei nur einer Option diese Ausgeben
            return newField * 10 + enemySafe[0];
        } else { //sonst random entscheiden
            Random rand = new Random();
            return newField * 10 + enemySafe[rand.nextInt(b + 1)];
        }
    }

    private int selectField() {
        //neue Bestimmung des nächsten Feldes wenn das eigentliche voll oder gewonnen schon ist
        int y = 0;
        if (possiField[1] == 0) {
            return possiField[0];
        }
        while (possiField[y] != 0) {
            if (possiField[y] == 1) {
                if (checkWin(2) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins == isPlayer) {
                        if (almostThreeInARow(1, isPlayer)) { // ai kann gewinnen
                            return 1;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(1, isPlayer)) {
                            return 1;
                        }
                    }
                }
                if (checkWin(4) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins == isPlayer) {
                        if (almostThreeInARow(1, isPlayer)) {
                            return 1;
                        }
                    }
                }
                if (checkWin(2) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isNotPlayer) && almostThreeInARow(1, isPlayer)) {
                            return 1;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isPlayer) || almostThreeInARow(1, isNotPlayer)) {
                            return 1;
                        }
                    }
                }
                if (checkWin(4) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isNotPlayer) || almostThreeInARow(1, isPlayer)) {
                            return 1;
                        }
                    }
                }
            }
            if (possiField[y] == 2) {
                if (checkWin(1) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins == isPlayer) {
                        if (almostThreeInARow(2, isPlayer)) { // ai kann gewinnen
                            return 2;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins == isPlayer) {
                        if (almostThreeInARow(1, isPlayer)) {
                            return 2;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins != isPlayer) {
                        if (almostThreeInARow(2, isPlayer) || almostThreeInARow(2, isNotPlayer)) {
                            return 2;
                        }
                    }
                }
                if (checkWin(1) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins != isPlayer) {
                        if (almostThreeInARow(2, isPlayer) || almostThreeInARow(2, isNotPlayer)) {
                            return 1;
                        }
                    }
                }
            }
            if (possiField[y] == 3) {
                if (checkWin(2) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(1) && whoWins == isPlayer) {
                        if (almostThreeInARow(3, isPlayer)) { // ai kann gewinnen
                            return 3;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins == isPlayer) {
                        if (almostThreeInARow(3, isPlayer)) {
                            return 3;
                        }
                    }
                }
                if (checkWin(6) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(6, isPlayer)) {
                            return 3;
                        }
                    }
                }
                if (checkWin(2) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(1) && whoWins != isPlayer) {
                        if (almostThreeInARow(2, isNotPlayer)) {
                            return 3;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isPlayer) || almostThreeInARow(1, isNotPlayer)) {
                            return 3;
                        }
                    }
                }
                if (checkWin(6) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isPlayer) || almostThreeInARow(1, isNotPlayer)) {
                            return 3;
                        }
                    }
                }
            }
            if (possiField[y] == 4) {
                if (checkWin(1) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins == isPlayer) {
                        if (almostThreeInARow(4, isPlayer)) { // ai kann gewinnen
                            return 4;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins == isPlayer) {
                        if (almostThreeInARow(5, isPlayer)) {
                            return 4;
                        }
                    }
                }
                if (checkWin(7) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(1) && whoWins != isPlayer) {
                        if (almostThreeInARow(4, isPlayer)) {
                            return 4;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins != isPlayer) {
                        if (almostThreeInARow(1, isPlayer) || almostThreeInARow(1, isNotPlayer)) {
                            return 4;
                        }
                    }
                }
            }
            if (possiField[y] == 5) {
                if (checkWin(1) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(5, isPlayer)) { // ai kann gewinnen
                            return 5;
                        }
                    }
                }
                if (checkWin(2) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins == isPlayer) {
                        if (almostThreeInARow(5, isPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(3) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins == isPlayer) {
                        if (almostThreeInARow(5, isPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(4) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins == isPlayer) {
                        if (almostThreeInARow(5, isPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(1) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(5, isPlayer) || almostThreeInARow(5, isNotPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(2) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins != isPlayer) {
                        if (almostThreeInARow(5, isPlayer) || almostThreeInARow(5, isNotPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(3) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(7) && whoWins != isPlayer) {
                        if (almostThreeInARow(5, isPlayer) || almostThreeInARow(5, isNotPlayer)) {
                            return 5;
                        }
                    }
                }
                if (checkWin(4) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins != isPlayer) {
                        if (almostThreeInARow(5, isPlayer) || almostThreeInARow(5, isNotPlayer)) {
                            return 1;
                        }
                    }
                }
            }
            if (possiField[y] == 6) {
                if (checkWin(3) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(6, isPlayer)) { // ai kann gewinnen
                            return 6;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(4) && whoWins == isPlayer) {
                        if (almostThreeInARow(6, isPlayer)) {
                            return 6;
                        }
                    }
                }
                if (checkWin(3) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(6, isPlayer) || almostThreeInARow(6, isNotPlayer)) {
                            return 6;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(4) && whoWins != isPlayer) {
                        if (almostThreeInARow(6, isPlayer) || almostThreeInARow(6, isNotPlayer)) {
                            return 6;
                        }
                    }
                }
            }
            if (possiField[y] == 7) {
                if (checkWin(1) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(4) && whoWins == isPlayer) {
                        if (almostThreeInARow(7, isPlayer)) { // ai kann gewinnen
                            return 7;
                        }
                    }
                }
                if (checkWin(5) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins == isPlayer) {
                        if (almostThreeInARow(7, isPlayer)) {
                            return 7;
                        }
                    }
                }
                if (checkWin(8) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(7, isPlayer)) {
                            return 7;
                        }
                    }
                }
                if (checkWin(1) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(4) && whoWins != isPlayer) {
                        if (almostThreeInARow(7, isPlayer) || almostThreeInARow(7, isNotPlayer)) {
                            return 7;
                        }
                    }
                }
                if (checkWin(5) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(3) && whoWins != isPlayer) {
                        if (almostThreeInARow(7, isPlayer) || almostThreeInARow(7, isNotPlayer)) {
                            return 7;
                        }
                    }
                }
                if (checkWin(8) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(7, isPlayer) || almostThreeInARow(7, isNotPlayer)) {
                            return 7;
                        }
                    }
                }
            }
            if (possiField[y] == 8) {
                if (checkWin(7) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins == isPlayer) {
                        if (almostThreeInARow(8, isPlayer)) { // ai kann gewinnen
                            return 8;
                        }
                    }
                }
                if (checkWin(2) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(5) && whoWins == isPlayer) {
                        if (almostThreeInARow(1, isPlayer)) {
                            return 2;
                        }
                    }
                }
                if (checkWin(2) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(5) && whoWins != isPlayer) {
                        if (almostThreeInARow(8, isPlayer) || almostThreeInARow(8, isNotPlayer)) {
                            return 8;
                        }
                    }
                }
                if (checkWin(7) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(9) && whoWins != isPlayer) {
                        if (almostThreeInARow(8, isPlayer) || almostThreeInARow(8, isNotPlayer)) {
                            return 8;
                        }
                    }
                }
            }
            if (possiField[y] == 9) {
                if (checkWin(3) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins == isPlayer) {
                        if (almostThreeInARow(9, isPlayer)) { // ai kann gewinnen
                            return 9;
                        }
                    }
                }
                if (checkWin(1) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(5) && whoWins == isPlayer) {
                        if (almostThreeInARow(9, isPlayer)) {
                            return 9;
                        }
                    }
                }
                if (checkWin(7) && whoWins == isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins == isPlayer) {
                        if (almostThreeInARow(9, isPlayer)) {
                            return 9;
                        }
                    }
                }
                if (checkWin(3) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(6) && whoWins != isPlayer) {
                        if (almostThreeInARow(9, isPlayer) || almostThreeInARow(9, isNotPlayer)) {
                            return 9;
                        }
                    }
                }
                if (checkWin(1) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(5) && whoWins != isPlayer) {
                        if (almostThreeInARow(9, isPlayer) || almostThreeInARow(9, isNotPlayer)) {
                            return 9;
                        }
                    }
                }
                if (checkWin(7) && whoWins != isPlayer)//wer gewonnen hat und ob
                {
                    if (checkWin(8) && whoWins != isPlayer) {
                        if (almostThreeInARow(9, isPlayer) || almostThreeInARow(9, isNotPlayer)) {
                            return 9;
                        }
                    }
                }
            }
            y++;
        }
        int x = 0; //sonst Bestimmung durch Zufall eines möglichen Feldes
        Random rand = new Random();
        while (possiField[x] != 0) {
            x++;
        }
        return possiField[rand.nextInt(x)];
    }

    boolean[] isChecked = new boolean[9];

    private void chooseField(int checkField) {
        //Bestimmung eines nächsten Kastens wenn das eine schon benutzt ist
        Map<Integer, int[]> possible = gameController.getneighbors(checkField);
        int count = 0;

        for (int i = 0; i < possible.get(checkField).length; i++) {
            if (checkWin(possible.get(checkField)[i]) == false) {
                counter++;
                possiField[counter] = possible.get(checkField)[i];
            }
        }
        if (possiField[0] == 0) {

        }
    }

    List<Integer> enemy_layout = new ArrayList<Integer>();

    public boolean almostThreeInARow(int enemyField, int whichPlayer) {//überprüft ob einer der Spieler im nächsten Zug  drei in einer Reihe hat
        int x = 0;
        for (int i = 1; i < 10; i++) {
            if (gameboard[enemyField * 10 + i] == whichPlayer) {
                enemy_layout.add(enemyField * 10 + i);
                x++;
            }
        }
        if (enemy_layout.contains(1)) {
            if (enemy_layout.contains(2) && gameboard[enemyField * 10 + 3] == 0) {
                return true;
            }
            if (enemy_layout.contains(3) && gameboard[enemyField * 10 + 2] == 0) {
                return true;
            }
            if (enemy_layout.contains(5) && gameboard[enemyField + 9] == 0) {
                return true;
            }
            if (enemy_layout.contains(9) && gameboard[enemyField + 5] == 0) {
                return true;
            }
            if (enemy_layout.contains(4) && gameboard[enemyField + 7] == 0) {
                return true;
            }
            if (enemy_layout.contains(7) && gameboard[enemyField + 5] == 0) {
                return true;
            }
        } else if (enemy_layout.contains(2) && gameboard[enemyField * 10 + 1] == 0 && enemy_layout.contains(3)) {
            return true;
        } else if (enemy_layout.contains(9) && gameboard[enemyField * 10 + 1] == 0 && enemy_layout.contains(5)) {
            return true;
        } else if (enemy_layout.contains(4) && gameboard[enemyField * 10 + 1] == 0 && enemy_layout.contains(7)) {
            return true;
        }
        if (enemy_layout.contains(2)) {
            if (enemy_layout.contains(5) && gameboard[enemyField * 10 + 8] == 0) {
                return true;
            }
            if (enemy_layout.contains(8) && gameboard[enemyField * 10 + 5] == 0) {
                return true;
            }
        } else if (enemy_layout.contains(8) && gameboard[enemyField * 10 + 2] == 0 && enemy_layout.contains(5)) {
            return true;
        }
        if (enemy_layout.contains(3)) {
            if (enemy_layout.contains(6) && gameboard[enemyField * 10 + 9] == 0) {
                return true;
            }
            if (enemy_layout.contains(9) && gameboard[enemyField * 10 + 6] == 0) {
                return true;
            }
            if (enemy_layout.contains(5) && gameboard[enemyField * 10 + 7] == 0) {
                return true;
            }
            if (enemy_layout.contains(7) && gameboard[enemyField * 10 + 5] == 0) {
                return true;
            }
        } else if (enemy_layout.contains(5) && gameboard[enemyField * 10 + 3] == 0 && enemy_layout.contains(7)) {
            return true;
        } else if (enemy_layout.contains(9) && gameboard[enemyField * 10 + 3] == 0 && enemy_layout.contains(6)) {
            return true;
        }
        if (enemy_layout.contains(4)) {
            if (enemy_layout.contains(5) && gameboard[enemyField * 10 + 6] == 0) {
                return true;
            }
            if (enemy_layout.contains(6) && gameboard[enemyField * 10 + 5] == 0) {
                return true;
            }
        } else if (enemy_layout.contains(5) && gameboard[enemyField * 10 + 4] == 0 && enemy_layout.contains(6)) {
            return true;
        }
        if (enemy_layout.contains(7)) {
            if (enemy_layout.contains(8) && gameboard[enemyField * 10 + 9] == 0) {
                return true;
            }
            if (enemy_layout.contains(9) && gameboard[enemyField * 10 + 8] == 0) {
                return true;
            }
        } else if (enemy_layout.contains(8) && gameboard[enemyField * 10 + 7] == 0 && enemy_layout.contains(9)) {
            return true;
        }
        return false;
    }

    private boolean justOneOrLessEnemy(int inspectField) {//überprüft die Ob die Anzahl der Gegner weniger als eins sind in einem Kasten
        int count = 0;
        for (int i = 1; i < 10; i++) {
            if (gameboard[inspectField * 10 + i] == isNotPlayer) {
                count++;
            }
        }
        if (count <= 1) {
            return true;
        }
        return false;

    }



    public boolean checkWin(int field) { // überprüft, ob ein Feld bereits gewonnen ist.
        field = field * 10;
        if (gameboard[field + 1] == 3) {
            if (gameboard[field + 2] == 3 && gameboard[field + 3] == 3) {
                whoWins = 3;
                return true;
            }
            if (gameboard[field + 4] == 3 && gameboard[field + 7] == 3) {
                whoWins = 3;
                return true;
            }
            if (gameboard[field + 5] == 3 && gameboard[field + 9] == 3) {
                whoWins = 3;
                return true;
            }
        } else if (gameboard[field + 2] == 3) {
            if (gameboard[field + 5] == 3 && gameboard[field + 8] == 3) {
                whoWins = 3;
                return true;
            }
        }
        if (gameboard[field + 3] == 3) {
            if (gameboard[field + 5] == 3 && gameboard[field + 7] == 3) {
                whoWins = 3;
                return true;
            }
            if (gameboard[field + 4] == 3 && gameboard[field + 7] == 3) {
                whoWins = 3;
                return true;
            }
            if (gameboard[field + 5] == 3 && gameboard[field + 9] == 3) {
                whoWins = 3;
                return true;
            }
        }
        if (gameboard[field + 4] == 3 && gameboard[field + 5] == 3 && gameboard[field + 6] == 3) {
            whoWins = 3;
            return true;
        }
        if (gameboard[field + 7] == 3 && gameboard[field + 8] == 3 && gameboard[field + 9] == 3) {
            whoWins = 3;
            return true;
        }
        if (gameboard[field + 1] == 5) {
            if (gameboard[field + 2] == 5 && gameboard[field + 3] == 5) {
                whoWins = 5;
                return true;
            }
            if (gameboard[field + 4] == 5 && gameboard[field + 7] == 5) {
                whoWins = 5;
                return true;
            }
            if (gameboard[field + 5] == 5 && gameboard[field + 9] == 5) {
                whoWins = 5;
                return true;
            }
        } else if (gameboard[field + 2] == 5) {
            if (gameboard[field + 5] == 5 && gameboard[field + 8] == 5) {
                whoWins = 5;
                return true;
            }
        }
        if (gameboard[field + 3] == 5) {
            if (gameboard[field + 5] == 5 && gameboard[field + 7] == 5) {
                whoWins = 5;
                return true;
            }
            if (gameboard[field + 4] == 5 && gameboard[field + 7] == 5) {
                whoWins = 5;
                return true;
            }
            if (gameboard[field + 5] == 5 && gameboard[field + 9] == 5) {
                whoWins = 5;
                return true;
            }
        }
        if (gameboard[field + 4] == 5 && gameboard[field + 5] == 5 && gameboard[field + 6] == 5) {
            whoWins = 5;
            return true;
        }
        if (gameboard[field + 7] == 5 && gameboard[field + 8] == 5 && gameboard[field + 9] == 5) {
            whoWins = 5;
            return true;
        }
        return false;
    }
}