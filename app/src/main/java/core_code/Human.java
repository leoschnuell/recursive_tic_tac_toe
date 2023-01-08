package core_code;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.arch.core.executor.TaskExecutor;

import com.example.picture_button.Board;
import com.example.picture_button.MainActivity;

import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Human implements Player {
    GameController gameController;
    boolean is_beginning = false;
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

        while (board.getLastButId() ==-1)
        {

        }
        }while (!gameController.checkMove(board.getLastButId()));

        return board.getLastButId();
    }
    @Override
    public void is_beginning(boolean b) {
        is_beginning = b;
    }

    @Override
    public String has_won() {
        return "Gebe eine tolle sieges nachicht an";
    }
}
