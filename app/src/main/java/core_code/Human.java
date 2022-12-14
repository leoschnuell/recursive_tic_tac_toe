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

        final int[] a = {0};
  /*      Executor executor = new Executor() {
            @Override
            public void execute(Runnable runnable) {
                runnable.run();
            }
        };
        executor.execute(new Runnable() {
            @Override
            public void run() {
                board.setLastButId(-1);
                while (board.getLastButId()==-1) {}
                a[0] = board.getLastButId();
            }
        });
*/
        /*
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                board.setLastButId(-1);
                while (board.getLastButId()==-1) {
                    System.out.println("stuck in loop");

                }
                a[0] = board.getLastButId();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        return a[0];
                    }
                });
            }
        });
*/

         AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                board.setLastButId(-1);
                while (board.getLastButId()==-1) {
                    System.out.println("stuck in loop");

                }
                a[0] = board.getLastButId();
            }
        });


        return a[0];

        //return mainActivity.get_vaild_input();

/*        //gameController.display();
        int i;
        do {
            System.out.println("chose a move (that's valid limited checks)");
            i = scanner.nextInt();
        } while (!(i > 10 && i < 100 && i % 10 != 0));
        return i;*/
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
