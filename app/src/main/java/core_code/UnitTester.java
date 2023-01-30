package core_code;

import com.example.picture_button.Board;

public class UnitTester implements Player {

    @Override
    public void setBoard(Board board) {

    }

    int[] testlist = {51, 19, 18, 17, 96};
    //int[] testlist= {12,23,34,45,56,67,78,89,99,97,76,65,54,43,32,21,11,11};
    //int[] testlist = {19,97,79,93,39,99,95,53,38,87,76,61,11,15,52,28,84,44,45,56,64,41,13,34,47,78,86,62,21,18,85,59,92,22,29,98,73,37,43,35,25,63,31,12};
    static int counter;

    public UnitTester() {
        counter = -1;
    }

    public UnitTester(int[] arr) {
        this();
        testlist = arr;
    }

    Human alt = new Human();

    @Override
    public int move(int lastMove) {
        counter++;
        if (counter < testlist.length)
            return testlist[counter];
        else {
            return alt.move(lastMove);
        }
    }

    @Override
    public void isBeginning(boolean b) {

    }

    @Override
    public String hasWon() {
        double per = (double) (counter + 1) / (double) testlist.length * 100;
        GameController.getGameControler().display();
        return per + "% der moves waren erfolgleich \n ";
    }
}
