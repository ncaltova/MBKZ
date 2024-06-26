package com.example.mbkz_semestral_work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.io.File

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

    override fun onResume() {
        val sharedPreferences = getSharedPreferences("com.example.mbkz_semestral_work", MODE_PRIVATE)
        val highest = sharedPreferences.getInt("score", 0)

        findViewById<TextView>(R.id.highScore).text = highest.toString()

        super.onResume()
    }
}