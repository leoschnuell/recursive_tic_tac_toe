package com.example.picture_button

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import core_code.Human


class MainMenu : AppCompatActivity() , View.OnClickListener  {
    val idTObutton: MutableMap<Int, View> = mutableMapOf<Int,View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu);
        val btn0 = findViewById<Button>(R.id.player_player_but);
        val btn1 = findViewById<Button>(R.id.player_ai_but);
        val btn2 = findViewById<Button>(R.id.ai_ai_but);
        btn0.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val b = Bundle()
            b.putString("p1", "Human" );
            b.putString("p2", "Human" );
            intent.putExtras(b) //Put your id to your next Intent
            startActivity(intent)
        }
        btn1.setOnClickListener{
            val intent = Intent(this,AIMenu::class.java)
            startActivity(intent);
        }
        btn2.setOnClickListener{
            val intent = Intent(this,AIMenu::class.java)
            startActivity(intent);
        }

    }

    override fun onClick(p0: View?) {
        println(p0)
    }
}