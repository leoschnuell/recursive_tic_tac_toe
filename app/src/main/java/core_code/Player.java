package core_code;

import android.annotation.SuppressLint;

import com.example.picture_button.Board;

public interface Player {
    //Player(GameControler)

    // the move function is called by the Board.kt
    // it is always wrapped in a method that makes is possible to take as much time a needed fo the Player to make a move
    int move(int lastMove);//lastMove = 1 means your move is the first move

    void setBoard(Board board); //{this.board = board;    }

}
