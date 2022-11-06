package core_code;
import java.util.Scanner;

public class Human implements Player {
    GameController gameController;
    boolean is_beginning = false;

    public Human() {
        gameController = GameController.getGameControler();
        gameController.getBoard(); // git ein int[100] als game bord zurück
        gameController.display(); // zeigt das game in der konsole an
    }

    Scanner scanner = new Scanner(System.in);

    @Override
    public int move(int lastMove) {
        gameController.display();
        int i;
        do {
            System.out.println("chose a move (that's valid limited checks)");
            i = scanner.nextInt();
        } while (!(i > 10 && i < 100 && i % 10 != 0));
        return i;
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
