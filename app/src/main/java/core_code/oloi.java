package core_code;

public class oloi extends Human {
    GameController gameControler;
    private int[] gamebord;
    public oloi() {
        gameControler = GameController.getGameControler();
        gamebord = gameControler.getBoard();
    }

    public int move(int lastMove) {
        int move = -1;
        int bestresult = -100;
        int field = lastMove %10 *10;
        for(int i = 1; i < 10; i++) {
            if (gamebord[field] == 0){
                gamebord[field+i] = 5;
                int result = minimax(gamebord, 0, false , lastMove);
                gamebord[field+i] = 0;
                if(result > bestresult){
                    bestresult = result;
                    move = field+i;
                }
            }

        }
        return move;
    }
    int[] score = {-1,0,1};
    public int minimax(int[] board, int depth, boolean isMax, int lastMove) {
        int winner = 2;
        winner = check(board ,lastMove % 10 * 10);
        if(winner != 2){
            return score[winner+1];
        }
        if (isMax) {
            int bestscore = -100;
            for (int i = 1; i < 10; i++) {
                if (board[lastMove % 10 * 10 + i] == 0) {
                    board[lastMove % 10 * 10 + i] = 5;
                    int score = minimax(board, depth, false, lastMove);
                    board[lastMove % 10 * 10 + i] = 0;
                    if (score > bestscore) {
                        bestscore = score;
                    }
                }
            }
            return bestscore;
        } else {
            int bestscore = 100;
            for (int i = 1; i < 10; i++) {
                if (board[lastMove % 10 * 10 + i] == 0) {
                    board[lastMove % 10 * 10 + i] = 3;
                    int score = minimax(board, depth, true, lastMove);
                    board[lastMove % 10 * 10 + i] = 0;
                    if (score < bestscore) {
                        bestscore = score;
                    }
                }
            }
            return bestscore;
        }
    }
    private int check(int[] gb, int field){
        int[] result = new int[8];
        result[0] = gb[field+ 1] + gb[field+2] + gb[field+3];
        result[1] = gb[field+4] + gb[field+5] + gb[field+6];
        result[2] = gb[field+7] + gb[field+8] + gb[field+9];
        result[3] = gb[field+1] + gb[field+4] + gb[field+7];
        result[4] = gb[field+2] + gb[field+5] + gb[field+8];
        result[5] = gb[field+3] + gb[field+6] + gb[field+9];
        result[6] = gb[field+1] + gb[field+5] + gb[field+9];
        result[7] = gb[field+3] + gb[field+5] + gb[field+7];

        for (int i = 0; i < 8; i++) {
            if (result[i] == 9)
                return -1;
            else if (result[i] == 15)
                return 1;
        }
        int count = 0;
        for(int i = 1; i< 10; i++){
            if(gb[field+i] == 0){
                count++;
            }
        }
        if(count == 0){
            return 0;
        }
        else {
            return 2;
        }
    }
}
