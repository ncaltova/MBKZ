package com.example.mbkz_semestral_work

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

class GameActivity : AppCompatActivity() {

    /**
     * Game loop handling user input and updating
     */
    private val gameLoop = GameLoop(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // gameLoop.updateInput(event) TODO
        return true
    }
}