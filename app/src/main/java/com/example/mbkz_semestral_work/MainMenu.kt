package com.example.mbkz_semestral_work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
    }

    /**
     * Method that starts a new game
     */
    fun newGame(view: View) {
        val newGame = Intent(this, GameActivity::class.java)
        startActivity(newGame)
    }
}