package core_code;

import com.example.picture_button.Board;

public class leo_alg implements Player {
    @Override
    public void setBoard(Board board) {

    }

    GameController gameController;
    private  int[] gamebord;

    public leo_alg() {
        gameController = GameController.getGameControler();
        gamebord = gameController.getBoard();
    }
    @Override
    public int move(int lastMove) {
        gamebord = gameController.getBoard();
        if(lastMove==1){return 55;}
        switch (lastMove%10){
            case 3:if(gamebord[30]!=0){
                System.out.println("noch kein bock"); return 100;
            }else if(gamebord[39]==0) {
                return 39;
            } else if (gamebord[36]==0) {
                return 36;
            }else if (gamebord[33]==0){
                return 33;
            }
                break;
            case 5:if(gamebord[50]!=0){
                System.out.println("noch kein bock"); return 100;
            }else
            if (gamebord[55]==0){
                if(gamebord[52]==0) {
                    return 52;
                } else if (gamebord[58]==0) {
                    return 58;
                }else {
                    if (gamebord[59]==0){
                        return 59;
                    } else if (gamebord[56]==0) {
                        return 56;
                    } else if (gamebord[53]==0) {
                        return 53;
                    }
                }
            }
                break;
            case 7:
                if(gamebord[70]!=0){
                    System.out.println("noch kein bock"); return 100;
                }else if(gamebord[79]==0) {
                    return 79;
                } else if (gamebord[78]==0) {
                    return 78;
                }else if (gamebord[77]==0){
                    return 77;
                }
                break;
            default:
                int k= lastMove%10;//kasten
                for (int i=1;i<10;i++){
                    if(i==3|i==5|i==7){i++;}
                    if(gamebord[k*10 +i]==0){
                        return k*10 +i;
                    }
                }

        }
        System.out.println("Unvorhergesehener case"); return 100;
    }

    @Override
    public void is_beginning(boolean b) {

    }

    @Override
    public String has_won() {
        return null;
    }
}