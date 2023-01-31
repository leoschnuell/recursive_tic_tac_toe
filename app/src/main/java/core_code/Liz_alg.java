package core_code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.picture_button.Board;


public class Liz_alg implements Player {

    GameController gameControler;
    private  int[] gameboard;
    int whoWins = 0;
    int counter;
    int isPlayer;
    int isNotPlayer;
    private int[] lessTwoField = new int[9];  //
    private  int[] freeField = new int[9]; //freie Felder in einem Kasten
    private int[] enemySafe = new int[9];
    private int[] possiField = new int[9];
    private int newField;
    public Liz_alg() {
        gameControler = GameController.getGameControler();
        gameboard = gameControler.getBoard();
       if(gameControler.getP1() == "Daisy"){
           isPlayer =  3;
           isNotPlayer = 5;
        }
        else {
        isNotPlayer = 3;
        isPlayer = 5;
        }
    }
    @Override
    public int move(int lastMove) {
        counter = 0;
        gameboard = gameControler.getBoard();
        newField = lastMove % 10; //Bestimmung des nächsten Kastens
        if(checkWin(newField)) //Falls der bestimmte Kasten gewonnen ist. Rausfinden, welche Kasten als nächstes Belegt werden sollen
        {
            for(int i = 0; i <= 8; i++) {//Zurücksetzen der gecheckten und möglichen Felder
                isChecked[i] = false;
                possiField[i] = 0;
            }
            chooseField(newField); //rausfinden, welche Kasten in Frage für den Move kommen
            newField = selectField(); //neuen Kasten bestimmen
        }

        int x = 0;
        int p = 0;
        for(int i = 0; i < 9; i++){
            freeField[i] = 0;
            enemySafe[i] = 0;
            lessTwoField[i] = 0;
        }
        for(int i = 1; i < 10; i++){
            if(gameboard[newField*10+i] == 0){ //Welche Felder potentiell benutzt werden können
                freeField[x] = i;
                x++;
            }
        }
        x = 0;
        int y = 0;
        int b = 0;
        while(freeField[y] != 0) {
            if (justOneOrLessEnemy(freeField[y])) {
                lessTwoField[b] = freeField[y];
                if(b > 8 ) {
                    b++;
                }
            }
           if(y >= 8 ) {
               break;
           }
           y++;
        }
        if(lessTwoField[1] == 0 && lessTwoField[0]%10 != 5){
            return (newField)*10 +lessTwoField[0];
        }
        else if(lessTwoField[2] == 0 && lessTwoField[1] != 0){
            if(lessTwoField[0] % 10 == 5){
                return (newField)*10 +lessTwoField[1];
            }
            if(lessTwoField[1] % 10 == 5){
                return (newField)*10 + lessTwoField[0];
            }
        }
        else if(lessTwoField[0] != 0 && lessTwoField[1] != 0 ){
            Random rand = new Random();
            int h = lessTwoField[rand.nextInt(b+1)];
            while( h == 0){
                h = lessTwoField[rand.nextInt(b+1)];
            }
            return (newField)*10 + h;
        }

        while(freeField[p] != 0  && p < 9){
                if(!almostThreeInARow(freeField[p],isNotPlayer)) {// welche freien Felder benutzt werden könen die es dem Gegner nicht ermöglichen nim nächsten Zu eine Rheie vollzubekommen
                    enemySafe[x] = freeField[p];
                x++;
            }
            p++;
        }
        if(enemySafe[2]  == 0){
            if(enemySafe[0]%10 == 5) //nochmal anschauen
            {
                enemySafe[0] = enemySafe[1];
                enemySafe[1] = 0;
            }
            if(enemySafe[1]%10 == 5){
                enemySafe[1] = 0;
            }
        }
        if(enemySafe[1] ==  0){
            return newField* 10 +enemySafe[0];
        }
        else{
            Random rand = new Random();
            return newField* 10 + enemySafe[rand.nextInt(b+1)];
        }
    }

    private int selectField() {
        int y = 0;
        if (possiField[1] == 0) {
            return possiField[0];
        }
        while(possiField[y] != 0) {
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
                        if (almostThreeInARow(1 , isPlayer) || almostThreeInARow(1, isNotPlayer)) {
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
        int x= 0;
        Random rand = new Random();
        while (possiField[x] != 0){
            x++;
        }
        return possiField[rand.nextInt(x)];
    }
    boolean[] isChecked = new boolean[9];
    private void chooseField(int checkField) {
        Map<Integer, int[]> possible = gameControler.getneighbors(checkField);
        int count = 0;

        for(int i = 0; i < possible.get(checkField).length; i++) {
            if (checkWin(possible.get(checkField)[i]) == false) {
                counter++;
                    possiField[counter] = possible.get(checkField)[i];
            }
        }
        if(possiField[0] == 0){

        }
    }

    List<Integer>enemy_layout = new ArrayList<Integer>();
    public boolean almostThreeInARow(int enemyField, int whichPlayer){
        int x = 0;
        for(int i = 1; i < 10; i++){
            if(gameboard[enemyField*10+i] == whichPlayer){
                enemy_layout.add(enemyField*10+i);
                x++;
            }
        }
        if(enemy_layout.contains(1)){
            if(enemy_layout.contains(2)  && gameboard[enemyField*10 + 3] == 0){
                return true;
            }
            if(enemy_layout.contains(3)  && gameboard[enemyField*10 + 2] == 0){
                return true;
            }
            if(enemy_layout.contains(5)  && gameboard[enemyField + 9] == 0){
                return true;
            }
            if(enemy_layout.contains(9)  && gameboard[enemyField + 5] == 0){
                return true;
            }
            if(enemy_layout.contains(4)  && gameboard[enemyField + 7] == 0){
                return true;
            }
            if(enemy_layout.contains(7)  && gameboard[enemyField + 5] == 0){
                return true;
            }
        }
        else if(enemy_layout.contains(2)  && gameboard[enemyField*10 + 1] == 0 && enemy_layout.contains(3) ){
            return true;
        }
        else if(enemy_layout.contains(9)  && gameboard[enemyField*10 + 1] == 0 && enemy_layout.contains(5) ){
            return true;
        }
        else if(enemy_layout.contains(4) && gameboard[enemyField*10 + 1] == 0 && enemy_layout.contains(7) ){
            return true;
        }
        if(enemy_layout.contains(2) ){
            if(enemy_layout.contains(5)  && gameboard[enemyField*10 + 8] == 0){
                return true;
            }
            if(enemy_layout.contains(8)  && gameboard[enemyField*10 + 5] == 0){
                return true;
            }
        }
        else if(enemy_layout.contains(8)  && gameboard[enemyField*10 + 2] == 0 && enemy_layout.contains(5) ){
            return true;
        }
        if(enemy_layout.contains(3) ){
            if(enemy_layout.contains(6)  && gameboard[enemyField*10 + 9] == 0){
                return true;
            }
            if(enemy_layout.contains(9)  && gameboard[enemyField*10 + 6] == 0){
                return true;
            }
            if(enemy_layout.contains(5)  && gameboard[enemyField*10 + 7] == 0){
                return true;
            }
            if(enemy_layout.contains(7)  && gameboard[enemyField*10 + 5] == 0){
                return true;
            }
        }
        else if(enemy_layout.contains(5)  && gameboard[enemyField*10 + 3] == 0 && enemy_layout.contains(7) ){
            return true;
        }
        else if(enemy_layout.contains(9)  && gameboard[enemyField*10 + 3] == 0 && enemy_layout.contains(6) ){
            return true;
        }
        if(enemy_layout.contains(4) ){
            if(enemy_layout.contains(5)  && gameboard[enemyField*10 + 6] == 0){
                return true;
            }
            if(enemy_layout.contains(6)  && gameboard[enemyField*10 + 5] == 0){
                return true;
            }
        }
        else if(enemy_layout.contains(5)  && gameboard[enemyField*10 + 4] == 0 && enemy_layout.contains(6) ){
            return true;
        }
        if(enemy_layout.contains(7) ){
            if(enemy_layout.contains(8)  && gameboard[enemyField*10 + 9] == 0){
                return true;
            }
            if(enemy_layout.contains(9)  && gameboard[enemyField*10 + 8] == 0){
                return true;
            }
        }
        else if(enemy_layout.contains(8)  && gameboard[enemyField*10 + 7] == 0 && enemy_layout.contains(9) ){
            return true;
        }
        return false;
    }
    private boolean justOneOrLessEnemy(int inspectField){
        int count = 0;
        for(int i = 1; i < 10; i++){
            if(gameboard[inspectField*10+i] == isNotPlayer)
            {
                count++;
            }
        }
        if(count <= 1){
            return true;
        }
        return false;

    }

    @Override
    public void setBoard(Board board) {

    }

    public boolean checkWin(int field) {
        field = field *10;
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
        if(gameboard[field + 4] == 3 && gameboard[field + 5] == 3 && gameboard[field + 6] == 3)
        {
            whoWins = 3;
            return true;
        }
        if(gameboard[field + 7] == 3 && gameboard[field + 8] == 3 && gameboard[field + 9] == 3)
        {
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
        if(gameboard[field + 4] == 5 && gameboard[field + 5] == 5 && gameboard[field + 6] == 5)
        {
            whoWins = 5;
            return true;
        }
        if(gameboard[field + 7] == 5 && gameboard[field + 8] == 5 && gameboard[field + 9] == 5)
        {
            whoWins = 5;
            return true;
        }
        return false;
    }
}