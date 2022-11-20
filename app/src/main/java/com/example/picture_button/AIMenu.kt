package com.example.picture_button

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import kotlin.reflect.typeOf


class AIMenu : AppCompatActivity() , View.OnClickListener  {
    val idTObutton: MutableMap<Int, View> = mutableMapOf<Int,View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_menu);
        val btn1 = findViewById<Button>(R.id.leo_alg_btn);
        btn1.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)

        }
        val btn2 = findViewById<Button>(R.id.san_alg_btn);
        btn2.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)

        }
        val btn0 = findViewById<Button>(R.id.liz_alg_btn);
        btn0.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true;
    }

    override fun onClick(p0: View?) {
        println(p0)
    }
}