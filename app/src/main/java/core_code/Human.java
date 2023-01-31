package core_code;

import com.example.picture_button.Board;


import java.util.Scanner;


public class Human implements Player {
    GameController gameController;
    private Board board;


    public Human() {
        gameController = GameController.getGameControler();

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    Scanner scanner = new Scanner(System.in);

    @Override
    public int move(int lastMove) {
        board.setLastButId(-1);
        do {

            while (board.getLastButId() == -1) {

            }
        } while (!gameController.checkMove(board.getLastButId()));

        return board.getLastButId();
    }

}
