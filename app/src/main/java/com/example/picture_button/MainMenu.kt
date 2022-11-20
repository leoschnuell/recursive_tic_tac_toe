package com.example.picture_button

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainMenu : AppCompatActivity() , View.OnClickListener  {
    val idTObutton: MutableMap<Int, View> = mutableMapOf<Int,View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu);
        val btn1 = findViewById<Button>(R.id.player_ai_but);
        val btn2 = findViewById<Button>(R.id.ai_ai_but);
        btn1.setOnClickListener{
            val Intent = Intent(this,AIMenu::class.java)
            startActivity(Intent);
        }
        btn2.setOnClickListener{
            val Intent = Intent(this,AIMenu::class.java)
            startActivity(Intent);
        }
        val btn0 = findViewById<Button>(R.id.player_player_but);
        btn1.setOnClickListener{
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent);
        }
    }

    override fun onClick(p0: View?) {
        println(p0)
    }
}