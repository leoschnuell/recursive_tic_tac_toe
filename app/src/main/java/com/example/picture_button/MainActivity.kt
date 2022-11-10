package com.example.picture_button

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() , View.OnClickListener  {
   // val idTObutton: MutableMap<Int, View> = mutableMapOf<Int,View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTime = System.currentTimeMillis()
        setContentView(R.layout.grid_test_test)


        val root = findViewById<GridLayout>(R.id.root)

        for (i in 0 until root.childCount) {
            val kasten: GridLayout =
                (root.getChildAt(i) as ConstraintLayout).getChildAt(0) as GridLayout

            for (j in 0 until kasten.childCount) {
                val kästschen = kasten.getChildAt(j)
                kästschen.setBackgroundColor(Color.BLACK)
                kästschen.tag = (i+1)*10 +j+1


                kästschen.setOnClickListener(this)
               // idTObutton[(i+1)*10 +j+1] =kästschen
            }
        }
        val difference = System.currentTimeMillis() - startTime
        println(difference)

        //val a1 = findViewById<Button>(R.id.a1)
        //a1.setBackgroundColor(0)

        /*


        val kasten2 = findViewById<ConstraintLayout>(R.id.kasten2)
        //   val a1 = kasten2.findViewById<Button>(R.id.a1)
        var i = 5;

        println((kasten2 as ConstraintLayout).childCount)
        for (index in 0 until kasten2.childCount) {
            val nextChild = kasten2.getChildAt(index)
            nextChild.setBackgroundColor(123456)
        }


        val resourceId = kasten2.resources.getIdentifier(
            "a$i",
            "id", this.packageName
        )
        val test = findViewById<View>(resourceId) as Button

        kasten2.setBackgroundColor(34)
        a1.text = "a1";
        a1.setPadding(0)
        a1.setBackgroundColor(0)
        test.setBackgroundColor(1)
*/
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            println(p0.tag)
        }

    }



}