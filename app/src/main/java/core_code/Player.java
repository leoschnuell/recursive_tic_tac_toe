package core_code;

import android.annotation.SuppressLint;

import com.example.picture_button.Board;

public interface Player {
    //Player(GameControler)

    int move(int lastMove);//lastMove = 1 means your move is the first move

    void is_beginning(boolean b);

    void setBoard(Board board); //{this.board = board;    }

    String has_won();
}
