package core_code;
import android.os.AsyncTask;

import com.example.picture_button.Board;
import com.example.picture_button.MainActivity;

import java.util.Scanner;

public class Human extends AsyncTask<Integer, String, Integer> implements Player {
    GameController gameController;
    boolean is_beginning = false;
    private Board board;


    public Human() {
        gameController = GameController.getGameControler();

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {


        return null;
    }

    Scanner scanner = new Scanner(System.in);

    @Override
    public int move(int lastMove) {
        return 55;
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
