package core_code;

import com.example.picture_button.Board;

public class Leo_alg implements Player {
    @Override
    public void setBoard(Board board) {

    }

    GameController gameController;
    private  int[] gameboard;

    public Leo_alg() {
        gameController = GameController.getgameController();
        gameboard = gameController.getBoard();
    }
    @Override
    public int move(int lastMove) {
        gameboard = gameController.getBoard();
        if(lastMove==1){return 55;}
        switch (lastMove%10){
            case 3:if(gameboard[30]!=0){
                System.out.println("noch kein bock"); return 100;
            }else if(gameboard[39]==0) {
                return 39;
            } else if (gameboard[36]==0) {
                return 36;
            }else if (gameboard[33]==0){
                return 33;
            }
                break;
            case 5:if(gameboard[50]!=0){
                System.out.println("noch kein bock"); return 100;
            }else
            if (gameboard[55]==0){
                if(gameboard[52]==0) {
                    return 52;
                } else if (gameboard[58]==0) {
                    return 58;
                }else {
                    if (gameboard[59]==0){
                        return 59;
                    } else if (gameboard[56]==0) {
                        return 56;
                    } else if (gameboard[53]==0) {
                        return 53;
                    }
                }
            }
                break;
            case 7:
                if(gameboard[70]!=0){
                    System.out.println("noch kein bock"); return 100;
                }else if(gameboard[79]==0) {
                    return 79;
                } else if (gameboard[78]==0) {
                    return 78;
                }else if (gameboard[77]==0){
                    return 77;
                }
                break;
            default:
                int k= lastMove%10;//kasten
                for (int i=1;i<10;i++){
                    if(i==3|i==5|i==7){i++;}
                    if(gameboard[k*10 +i]==0){
                        return k*10 +i;
                    }
                }

        }
        System.out.println("Unvorhergesehener case"); return 100;
    }

}