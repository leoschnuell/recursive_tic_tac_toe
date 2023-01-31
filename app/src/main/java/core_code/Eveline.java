package core_code;

import static java.lang.Math.max;

import com.example.picture_button.Board;

public class Eveline implements Player {
    GameController gameControler;
    private int[] gameboard;
    private int last;
    private int[] myMove = new int[99];
    private int[] scoreboard = new int[99];
    int isPlayer;
    int isNotPlayer;
    private Board board;

    public Eveline() {
        gameControler = GameController.getGameControler();
        gameboard = gameControler.getBoard();
        if (board.getP1() == Board.playerType.EVELINE) {
            isPlayer = 3;
            isNotPlayer = 5;
        } else {
            isNotPlayer = 3;
            isPlayer = 5;
        }
    }

    public int move(int lastMove) {
        int tracks = 0;
        last = lastMove;
        int newMove = -1;
        int score;
        int field = lastMove % 10 * 10;
        ;
        int bestscore = 100;
        for (int i = 1; i <= 9; i++) {
            if (gameboard[field + i] == 0) {
                gameboard[field + i] = isPlayer;
                score = minimax(gameboard, 6, 100, -100, false);
                gameboard[field + i] = 0;
                if (bestscore < score) {
                    bestscore = score;
                    newMove = field + i;
                }
            }
        }
        return newMove;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }


    public void is_beginning(boolean b) {
    }


    public String has_won() {
        return null;
    }

    int tracker;
    int[] tracks = new int[7];

    int minimax(int[] gameboard, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 6) {
            tracks[0] = last % 10;
        }
        if (depth == 0) { //checkt bis zu einer Tiefe (vorangehende Moves) bis 6 sonst zu viele Mögliche Sachen
            for (int i = 0; i < 6; i++) {
                tracks[i] = 0;
            }
            return evaluate();
        }
        if (isMax) {
            int field = tracks[6 - depth] % 10 * 10;
            int bestscore = -100;
            for (int i = 0; i < 9; i++) {
                int score = -100;
                if (gameboard[field + i] == 0) {
                    gameboard[field + i] = isPlayer;
                    tracks[6 - depth + 1] = i;
                    score = minimax(gameboard, depth - 1, alpha, beta, false);
                    gameboard[field + i] = 0;
                    gameboard[i] = 0;
                    if (score > bestscore) {
                        bestscore = score;
                    }
                    alpha = max(score, alpha);
                    if (alpha <= beta) {
                        break;
                    }
                }
            }
            return bestscore;
        } else {
            int field = tracks[6 - depth] % 10 * 10;
            int bestscore = 100;
            for (int i = 0; i < 9; i++) {
                int scor = 100;
                if (gameboard[field + i] == 0) {
                    gameboard[field + i] = isNotPlayer;
                    tracks[6 - depth + 1] = i;
                    scor = minimax(gameboard, depth - 1, alpha, beta, true);
                    gameboard[field + i] = 0;
                    if (scor > bestscore) {
                        bestscore = scor;
                    }
                    beta = Math.min(scor, beta);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestscore;
        }
    }

    private int evaluate() {
        for (int field = 0; field < 100; field = field + 10) {
            //zwei in einer Reihe +2
            //drei in einer Reihe
            if (gameboard[field + 5] == 0) {
                scoreboard[field + 5] = scoreboard[field + 5] + 1;
            }
            if (gameboard[field + 1] == isPlayer) {
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 4] = scoreboard[field + 4] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }

            }
            if (gameboard[field + 2] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] + 5;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 8] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }
            }
            if (gameboard[field + 3] == isPlayer) {
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] + 5;
                    }
                }
            }
            if (gameboard[field + 4] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                    if (gameboard[field + 6] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 1] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
            }
            if (gameboard[field + 5] == isPlayer) {
                if (gameboard[field + 1] == 0) {
                    scoreboard[field + 1] = scoreboard[field + 1] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] + 5;
                    }
                }
                if (gameboard[field + 2] == 0) {
                    scoreboard[field + 2] = scoreboard[field + 2] + 1;
                    if (gameboard[field + 8] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] + 5;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                    if (gameboard[field + 6] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                    if (gameboard[field + 4] == isPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] + 5;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                    if (gameboard[field + 2] == isPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] + 5;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 1] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 6] == 3) {
                if (gameboard[field + 3] == 0) {
                    scoreboard[field + 3] = scoreboard[field + 3] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 3] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 7] == isPlayer) {
                if (gameboard[field + 4] == 0) {
                    scoreboard[field + 4] = scoreboard[field + 4] + 1;
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 8] = scoreboard[field + 8] + 5;
                    }
                }
            }
            if (gameboard[field + 8] == isPlayer) {
                if (gameboard[field + 7] == 0) {
                    scoreboard[field + 7] = scoreboard[field + 7] + 1;
                    if (gameboard[field + 9] == isPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] + 5;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] + 1;
                    if (gameboard[field + 7] == isPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] + 5;
                    }
                }
            }
            if (gameboard[field + 9] == isPlayer) {
                if (gameboard[field + 5] == 0) {
                    scoreboard[field + 5] = scoreboard[field + 5] + 1;
                }
                if (gameboard[field + 8] == 0) {
                    scoreboard[field + 8] = scoreboard[field + 8] + 1;
                }
                if (gameboard[field + 6] == 0) {
                    scoreboard[field + 6] = scoreboard[field + 6] + 1;
                }
            }

            //mitte zu besetzen ?

            //zwei blocken
            if (gameboard[field + 1] == isNotPlayer) {
                if (gameboard[field + 2] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 4] = scoreboard[field + 4] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }

            }
            if (gameboard[field + 2] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 8] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }
            }
            if (gameboard[field + 3] == isNotPlayer) {
                if (gameboard[field + 6] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 5] = scoreboard[field + 5] - 2;
                    }
                }
            }
            if (gameboard[field + 4] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 5] == 0) {
                    if (gameboard[field + 6] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 1] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
            }
            if (gameboard[field + 5] == isNotPlayer) {
                if (gameboard[field + 1] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 1] = scoreboard[field + 1] - 2;
                    }
                }
                if (gameboard[field + 2] == 0) {
                    if (gameboard[field + 8] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 3] == 0) {
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] - 2;
                    }
                }
                if (gameboard[field + 4] == 0) {
                    if (gameboard[field + 6] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 6] == 0) {
                    if (gameboard[field + 4] == isNotPlayer) {
                        scoreboard[field + 6] = scoreboard[field + 6] - 2;
                    }
                }
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 8] == 0) {
                    if (gameboard[field + 2] == isNotPlayer) {
                        scoreboard[field + 2] = scoreboard[field + 2] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    if (gameboard[field + 1] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
            if (gameboard[field + 6] == isNotPlayer) {
                if (gameboard[field + 3] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 3] = scoreboard[field + 3] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    if (gameboard[field + 3] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
            if (gameboard[field + 7] == isNotPlayer) {
                if (gameboard[field + 8] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 8] = scoreboard[field + 8] - 2;
                    }
                }
            }
            if (gameboard[field + 8] == isNotPlayer) {
                if (gameboard[field + 7] == 0) {
                    if (gameboard[field + 9] == isNotPlayer) {
                        scoreboard[field + 7] = scoreboard[field + 7] - 2;
                    }
                }
                if (gameboard[field + 9] == 0) {
                    scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    if (gameboard[field + 7] == isNotPlayer) {
                        scoreboard[field + 9] = scoreboard[field + 9] - 2;
                    }
                }
            }
        }
        int fieldscore = 0;
        for (int i = 1; i <= 9; i++) {
            fieldscore = fieldscore + scoreboard[tracks[5] + i];

        }
        return fieldscore;
    }

}
