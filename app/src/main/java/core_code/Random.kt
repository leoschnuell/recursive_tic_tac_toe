package core_code

import com.example.picture_button.Board
import java.util.*

class Random : Player {
    private var gameController = GameController.getgameController()

    override fun move(lastMove: Int): Int {
        val possibleMoves = LinkedList<Int>()


        for (i in 0 until 9) {
            for (j in 0 until 10) {
                val id = ((i + 1) * 10 + j + 1)
                if (gameController.checkMove(id)) {
                    possibleMoves.add(id);
                }
            }

        }
        return possibleMoves.random()
    }
        override fun setBoard(board: Board?) {
        }

    }