package com.example.mbkz_semestral_work

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mbkz_semestral_work.game.GameState
import com.example.mbkz_semestral_work.game.GameView

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        if (gameView.game != null) {
            bundle.putSerializable("game", gameView.game?.gameState)
        }
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)

        gameView.gameState = bundle.getSerializable("game") as? GameState
    }

    override fun onBackPressed() {
        if (gameView.game?.over == false && gameView.game?.pause == false)
            gameView.game?.pause = true
        else {
            gameView.cacheHighScore()
            super.onBackPressed()
        }
    }

    /**
     * Pause game
     */
    override fun onPause() {
        super.onPause()
        if (gameView.game?.over == false)
            gameView.game?.pause = true
    }

}