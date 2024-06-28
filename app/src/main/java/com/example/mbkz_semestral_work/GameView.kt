package com.example.mbkz_semestral_work

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.ListFormatter.Width
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.mbkz_semestral_work.game.Game
import com.example.mbkz_semestral_work.utils.Input
import com.example.mbkz_semestral_work.utils.InputProcessor
import com.example.mbkz_semestral_work.utils.InputType

class GameView(
    /**
     * Parent activity
     */
    private val context: Context

) : SurfaceView(context) {

    /**
     * Time delta, set by game loop
     */
    var timeDelta : Float = 0f

    /**
     * Input processor
     */
    private val inputProcessor = InputProcessor()

    /**
     * Running game thread
     */
    private var gameLoop = GameLoop(this)

    /**
     * Game logic
     */
    var game : Game? = null

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                gameLoop = GameLoop(this@GameView)
                gameLoop.running = true
                gameLoop.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                var retry = true
                gameLoop.running = false
                while (retry) {
                    try {
                        gameLoop.join()
                        retry = false
                    } catch (_: InterruptedException) {
                    }
                }
            }
        })
    }

    /**
     * Registers touch event to input processor
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        inputProcessor.addInput(event)
        return performClick()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (game == null) {
            this.game = Game(this, inputProcessor)
        }
    }

    public override fun onDraw(canvas: Canvas) {
        if (game == null) return

        val result = game?.updateGame(timeDelta)

        game?.draw(canvas)

        chceckState(result)
    }

    /**
     * Returns to main menu and saves high score
     */
    private fun chceckState(result : Pair<Boolean, Int>?) {
        if (result?.first == false && game?.over == false) {
            game?.over = true
        }
    }

    fun exit(score : Int) {
        cacheHighScore(score)
        game?.over = true
        val newGame = Intent(context, MainMenu::class.java)
        this.context.startActivity(newGame)
    }

    fun cacheHighScore(score: Int?) {
        if (score == null) return

        val sharedPreferences = context.getSharedPreferences("com.example.mbkz_semestral_work", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val highest = sharedPreferences.getInt("score", 0)
        editor.putInt("score", if (highest < score) score else highest)
        editor.apply();
    }
}