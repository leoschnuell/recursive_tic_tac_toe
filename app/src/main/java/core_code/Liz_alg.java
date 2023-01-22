package core_code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.example.picture_button.Board;


public class Liz_alg implements Player {

    GameController gameControler;
    private  int[] gameboard;
    int counter;
    private int[] less_two_field = new int[9];
    private  int[] free_field = new int[9];
    private int[] enemie_safe = new int[9];
    private int[] possi_field = new int[9];
    private int new_field;
    public Liz_alg() {
        gameControler = GameController.getGameControler();
        gameboard = gameControler.getBoard();
    }
    @Override
    public int move(int lastMove) {
        counter = 0;
        gameboard = gameControler.getBoard();
        new_field = lastMove % 10;
        if(check_win(new_field))
        {
            for(int i = 0; i <= 8; i++) {
                isChecked[i] = false;
                possi_field[i] = 0;
            }
            choose_field(new_field);
            new_field = select_field();
        }

        int x = 0;
        int p = 0;
        for(int i = 0; i < 9; i++){
            free_field[i] = 0;
            enemie_safe[i] = 0;
            less_two_field[i] = 0;
        }
        for(int i = 1; i < 10; i++){
            if(gameboard[new_field*10+i] == 0){ //welche Felder potetiell benutztwerden können
                free_field[x] = i;
                x++;
            }
        }
        x = 0;
        int y = 0;
        int b = 0;
        while(free_field[y] != 0) {
            if (just_one_or_less_enemie(free_field[y])) {
                less_two_field[b] = free_field[y];
                if(b > 8 ) {
                    b++;
                }
            }
           if(y >= 8 ) {
               break;
           }
           y++;
        }
        if(less_two_field[1] == 0 && less_two_field[0]%10 != 5){
            return (new_field)*10 +less_two_field[0];
        }
        else if(less_two_field[2] == 0 && less_two_field[1] != 0){
            if(less_two_field[0] % 10 == 5){
                return (new_field)*10 +less_two_field[1];
            }
            if(less_two_field[1] % 10 == 5){
                return (new_field)*10 + less_two_field[0];
            }
        }
        else if(less_two_field[0] != 0 && less_two_field[1] != 0 ){
            Random rand = new Random();
            int h = less_two_field[rand.nextInt(b+1)];
            while( h == 0){
                h = less_two_field[rand.nextInt(b+1)];
            }
            return (new_field)*10 + h;
        }

        while(free_field[p] != 0  && p < 812){
                if(!almost_three_in_a_row(free_field[p])) {// welche freien Felder benutzt werden könen die es dem egner nicht ermöglichen nim nächsten Zu eine Rheie vollzubekommen
                    enemie_safe[x] = free_field[p];
                x++;
            }
            p++;
        }
        if(enemie_safe[2]  == 0){
            if(enemie_safe[0]%10 == 5)
            {
                enemie_safe[0] = enemie_safe[1];
                enemie_safe[1] = 0;
            }
            if(enemie_safe[1]%10 == 5){
                enemie_safe[1] = 0;
            }
        }
        if(enemie_safe[1] ==  0){
            return new_field* 10 +enemie_safe[0];
        }
        else{
            Random rand = new Random();
            return new_field* 10 + enemie_safe[rand.nextInt(b+1)];
        }
    }

    private int select_field() {
        int x= 0;
        Random rand = new Random();
        while (possi_field[x] != 0){
            x++;
        }
        return possi_field[rand.nextInt(x)];
    }
    boolean[] isChecked = new boolean[9];
    private void choose_field(int check_field) {
        for(int i = 0; i <= 8; i++){
            for (int x= i; x<= 8; x++){
               if(possi_field[i] == possi_field[x] && possi_field[i] != 0){
                   possi_field[x]= 0;
                   int d = x;
                   counter--;
                   while(d < 8){
                       possi_field[d] = possi_field[d+1];
                       d++;
                   }
               }
            }
        }

        if(check_field == 1 && !isChecked[0]){
            isChecked[0] = true;
            if(!check_win(2) && !isChecked[1]){
                isChecked[1] = true;
                possi_field[counter] = 2;
                counter++;

            }
            else {
                isChecked[1] = true;
                choose_field(2);
            }
            if(!check_win(3) && !isChecked[2]){
                isChecked[2] = true;
                possi_field[counter] = 3;
                counter++;
            }
            else{
                isChecked[2] = true;
                choose_field(3);
            }
        }
        else if(check_field == 2 && !isChecked[1]){
            isChecked[1] = true;
            if(!check_win(3) && !isChecked[2]){
                isChecked[2] = true;
                possi_field[counter] = 3;
                counter++;
            }
            else{
                isChecked[2] = true;
                choose_field(3);
            }
            if(!check_win(1) && !isChecked[0]){
                isChecked[0] = true;
                possi_field[counter] = 1;
                counter++;
            }
            else{
                isChecked[0] = true;
                choose_field(1);
            }
            if(!check_win(5) && !isChecked[4]){
                isChecked[4] = true;
                possi_field[counter] = 5;
                counter++;
            }
            else{
                isChecked[4] = true;
                choose_field(5);
            }
        }
        else if(check_field == 3  && !isChecked[2]){
            isChecked[2] = true;
            if(!check_win(2) && !isChecked[1]){
                isChecked[1] = true;
                possi_field[counter] = 3;
                counter++;
            }
            else{
                isChecked[1] = true;
                choose_field(2);
            }
            if(!check_win(6) && !isChecked[5]){
                isChecked[5] = true;
                possi_field[counter] = 6;
                counter++;
            }
            else{
                isChecked[5] = true;
                choose_field(6);
            }
        }
        else if(check_field == 4  && !isChecked[3]){
            isChecked[3] = true;
            if(!check_win(1)){
                isChecked[0] = true;
                possi_field[counter] = 1;
                counter++;
            }
            else{
                isChecked[0] = true;
                choose_field(1);
            }
            if(!check_win(5) && !isChecked[4]){
                isChecked[4] = true;
                possi_field[counter] = 5;
                counter++;
            }
            else{
                isChecked[4] = true;
                choose_field(5);
            }
            if(!check_win(7) && !isChecked[6]){
                isChecked[6] = true;
                possi_field[counter] = 7;
                counter++;
            }
            else{
                isChecked[6] = true;
                choose_field(7);
            }
        }
        else if(check_field == 5  && !isChecked[4]){
            isChecked[4] = true;
            if(!check_win(2) && !isChecked[1]){
                isChecked[1] = true;
                possi_field[counter] = 2;
                counter++;
            }
            else{
                isChecked[1] = true;
                choose_field(2);
            }
            if(!check_win(4) && !isChecked[3]){
                isChecked[3] = true;
                possi_field[counter] = 4;
                counter++;
            }
            else{
                isChecked[3] = true;
                choose_field(4);
            }
            if(!check_win(6) && !isChecked[5]){
                isChecked[5] = true;
                possi_field[counter] = 6;
                counter++;
            }
            else{
                isChecked[5] = true;
                choose_field(6);
            }
            if(!check_win(8) && !isChecked[8]){
                isChecked[8] = true;
                possi_field[counter] = 8;
                counter++;
            }
            else{
                isChecked[8] = true;
                choose_field(8);
            }
        }
        else if(check_field == 6  && !isChecked[5]){
            isChecked[5] = true;
            if(!check_win(3) && !isChecked[2]){
                isChecked[2] = true;
                possi_field[counter] = 6;
                counter++;
            }
            else{
                isChecked[2] = true;
                choose_field(3);
            }
            if(!check_win(5) && !isChecked[4]){
                isChecked[4] = true;
                possi_field[counter] = 5;
                counter++;
            }
            else{
                isChecked[4] = true;
                choose_field(5);
            }
            if(!check_win(9) && !isChecked[8]){
                isChecked[8] = true;
                possi_field[counter] = 9;
                counter++;
            }
            else{
                isChecked[8] = true;
                choose_field(9);
            }
        }
        else if(check_field == 7  && !isChecked[6]){
            isChecked[6] = true;
            if(!check_win(4) && !isChecked[3]){
                isChecked[3] = true;
                possi_field[counter] = 4;
                counter++;
            }
            else{
                isChecked[3] = true;
                choose_field(4);
            }
            if(!check_win(8) && !isChecked[7]){
                isChecked[7] = true;
                possi_field[counter] = 8;
                counter++;
            }
            else{
                isChecked[7] = true;
                choose_field(8);
            }
        }
        else if(check_field == 8  && !isChecked[7]){
            isChecked[7] = true;
            if(!check_win(5)){
                isChecked[4] = true;
                possi_field[counter] = 5;
                counter++;
            }
            else{
                isChecked[4] = true;
                choose_field(5);
            }
            if(!check_win(7) && !isChecked[6]) {
                isChecked[6] = true;
                possi_field[counter] = 7;
                counter++;
            }
            else{
                isChecked[6] = true;
                choose_field(7);
            }
            if(!check_win(9) && !isChecked[8]){
                isChecked[8] = true;
                possi_field[counter] = 9;
                counter++;
            }
            else{
                isChecked[8] = true;
                choose_field(9);
            }
        }
        else if(check_field == 9 && !isChecked[8]) {
            isChecked[8] = true;
            if (!check_win(6) && !isChecked[5]) {
                isChecked[5] = true;
                possi_field[counter] = 6;
                counter++;
            } else {
                isChecked[5] = true;
                choose_field(6);
            }
            if (!check_win(8) && !isChecked[7]) {
                isChecked[7] = true;
                possi_field[counter] = 8;
                counter++;
            } else {
                isChecked[7] = true;
                choose_field(8);
            }
        }
    }

    List<Integer>enemie_layout = new ArrayList<Integer>();
    public boolean almost_three_in_a_row(int enemie_field){
        int x = 0;
        for(int i = 1; i < 10; i++){
            if(gameboard[enemie_field*10+i] == 3){
                enemie_layout.add(enemie_field*10+i);
                x++;
            }
        }
        if(enemie_layout.contains(1)){
            if(enemie_layout.contains(2)  && gameboard[enemie_field*10 + 3] == 0){
                return true;
            }
            if(enemie_layout.contains(3)  && gameboard[enemie_field*10 + 2] == 0){
                return true;
            }
            if(enemie_layout.contains(5)  && gameboard[enemie_field + 9] == 0){
                return true;
            }
            if(enemie_layout.contains(9)  && gameboard[enemie_field + 5] == 0){
                return true;
            }
            if(enemie_layout.contains(4)  && gameboard[enemie_field + 7] == 0){
                return true;
            }
            if(enemie_layout.contains(7)  && gameboard[enemie_field + 5] == 0){
                return true;
            }
        }
        else if(enemie_layout.contains(2)  && gameboard[enemie_field*10 + 1] == 0 && enemie_layout.contains(3) ){
            return true;
        }
        else if(enemie_layout.contains(9)  && gameboard[enemie_field*10 + 1] == 0 && enemie_layout.contains(5) ){
            return true;
        }
        else if(enemie_layout.contains(4) && gameboard[enemie_field*10 + 1] == 0 && enemie_layout.contains(7) ){
            return true;
        }
        if(enemie_layout.contains(2) ){
            if(enemie_layout.contains(5)  && gameboard[enemie_field*10 + 8] == 0){
                return true;
            }
            if(enemie_layout.contains(8)  && gameboard[enemie_field*10 + 5] == 0){
                return true;
            }
        }
        else if(enemie_layout.contains(8)  && gameboard[enemie_field*10 + 2] == 0 && enemie_layout.contains(5) ){
            return true;
        }
        if(enemie_layout.contains(3) ){
            if(enemie_layout.contains(6)  && gameboard[enemie_field*10 + 9] == 0){
                return true;
            }
            if(enemie_layout.contains(9)  && gameboard[enemie_field*10 + 6] == 0){
                return true;
            }
            if(enemie_layout.contains(5)  && gameboard[enemie_field*10 + 7] == 0){
                return true;
            }
            if(enemie_layout.contains(7)  && gameboard[enemie_field*10 + 5] == 0){
                return true;
            }
        }
        else if(enemie_layout.contains(5)  && gameboard[enemie_field*10 + 3] == 0 && enemie_layout.contains(7) ){
            return true;
        }
        else if(enemie_layout.contains(9)  && gameboard[enemie_field*10 + 3] == 0 && enemie_layout.contains(6) ){
            return true;
        }
        if(enemie_layout.contains(4) ){
            if(enemie_layout.contains(5)  && gameboard[enemie_field*10 + 6] == 0){
                return true;
            }
            if(enemie_layout.contains(6)  && gameboard[enemie_field*10 + 5] == 0){
                return true;
            }
        }
        else if(enemie_layout.contains(5)  && gameboard[enemie_field*10 + 4] == 0 && enemie_layout.contains(6) ){
            return true;
        }
        if(enemie_layout.contains(7) ){
            if(enemie_layout.contains(8)  && gameboard[enemie_field*10 + 9] == 0){
                return true;
            }
            if(enemie_layout.contains(9)  && gameboard[enemie_field*10 + 8] == 0){
                return true;
            }
        }
        else if(enemie_layout.contains(8)  && gameboard[enemie_field*10 + 7] == 0 && enemie_layout.contains(9) ){
            return true;
        }
        return false;
    }
    private boolean just_one_or_less_enemie(int inspect_field){
        int count = 0;
        for(int i = 1; i < 10; i++){
            if(gameboard[inspect_field*10+i] == 3)
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
    public void is_beginning(boolean b) {
    }

    @Override
    public void setBoard(Board board) {

    }

    @Override
    public String has_won() {
        return null;
    }
    public boolean check_win(int field) {
        field = field *10;
        if (gameboard[field + 1] == 3) {
            if (gameboard[field + 2] == 3 && gameboard[field + 3] == 3) {
                return true;
            }
            if (gameboard[field + 4] == 3 && gameboard[field + 7] == 3) {
                return true;
            }
            if (gameboard[field + 5] == 3 && gameboard[field + 9] == 3) {
                return true;
            }
        } else if (gameboard[field + 2] == 3) {
            if (gameboard[field + 5] == 3 && gameboard[field + 8] == 3) {
                return true;
            }
        }
        if (gameboard[field + 3] == 3) {
            if (gameboard[field + 5] == 3 && gameboard[field + 7] == 3) {
                return true;
            }
            if (gameboard[field + 4] == 3 && gameboard[field + 7] == 3) {
                return true;
            }
            if (gameboard[field + 5] == 3 && gameboard[field + 9] == 3) {
                return true;
            }
        }
        if(gameboard[field + 4] == 3 && gameboard[field + 5] == 3 && gameboard[field + 6] == 3)
        {
            return true;
        }
        if(gameboard[field + 7] == 3 && gameboard[field + 8] == 3 && gameboard[field + 9] == 3)
        {
            return true;
        }
        if (gameboard[field + 1] == 5) {
            if (gameboard[field + 2] == 5 && gameboard[field + 3] == 5) {
                return true;
            }
            if (gameboard[field + 4] == 5 && gameboard[field + 7] == 5) {
                return true;
            }
            if (gameboard[field + 5] == 5 && gameboard[field + 9] == 5) {
                return true;
            }
        } else if (gameboard[field + 2] == 5) {
            if (gameboard[field + 5] == 5 && gameboard[field + 8] == 5) {
                return true;
            }
        }
        if (gameboard[field + 3] == 5) {
            if (gameboard[field + 5] == 5 && gameboard[field + 7] == 5) {
                return true;
            }
            if (gameboard[field + 4] == 5 && gameboard[field + 7] == 5) {
                return true;
            }
            if (gameboard[field + 5] == 5 && gameboard[field + 9] == 5) {
                return true;
            }
        }
        if(gameboard[field + 4] == 5 && gameboard[field + 5] == 5 && gameboard[field + 6] == 5)
        {
            return true;
        }
        if(gameboard[field + 7] == 5 && gameboard[field + 8] == 5 && gameboard[field + 9] == 5)
        {
            return true;
        }
        return false;
    }
}