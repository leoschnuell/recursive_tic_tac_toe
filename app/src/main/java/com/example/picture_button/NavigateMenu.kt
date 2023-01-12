package com.example.picture_button

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment


class NavigateMenu : AppCompatActivity() {
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTime = System.currentTimeMillis()
        setContentView(R.layout.navigate_menu)

        val navHomeFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController =navHomeFragment.navController

    }


}