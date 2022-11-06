package core_code;

public interface Player {
    //Player(GameControler)

    int move(int lastMove);//lastMove = 1 means your move is the first move

    void is_beginning(boolean b);

    String has_won();
}
