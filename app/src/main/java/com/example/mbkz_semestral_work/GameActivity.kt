package com.example.mbkz_semestral_work

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
    }

    /**
     * Save game state on application pause
     */
    override fun onPause() {
        this.gameView.cacheHighScore(gameView.game?.getScore())
        gameView.game?.clearScore()
        super.onPause()
    }

}