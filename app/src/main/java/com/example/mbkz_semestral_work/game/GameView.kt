package com.example.mbkz_semestral_work.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.mbkz_semestral_work.MainMenu
import com.example.mbkz_semestral_work.utils.InputProcessor
import kotlin.math.abs

class GameView(
    /**
     * Parent activity
     */
    private val context: Context

) : SurfaceView(context) {

    /**
     * Time delta, set by game loop
     */
    var timeDelta = 0f

    /**
     * Input processor
     */
    private val inputProcessor = InputProcessor()

    /**
     * Running game thread
     */
    private var gameLoop = GameLoop(this)

    /**
     * State of game, if not null game will return to given state
     */
    var gameState : GameState? = null

    /**
     * Game logic
     */
    var game : Game? = null
        private set

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
        inputProcessor.inputQueue.offer(event)
        return performClick()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (game == null) {
            game = Game(this, inputProcessor)

            if (gameState != null) {
                game?.over = gameState!!.over
                game?.score = gameState!!.score
                game?.pause = !gameState!!.over

                val scale = Position(
                    this.width / gameState!!.width,
                    this.height / gameState!!.height
                )

                val pillars = gameState!!.pillars as MutableList<Pillar>

                pillars.forEach{
                    it.height *= scale.y
                    it.position.x *= scale.x
                    it.position.y *= scale.y
                }

                gameState!!.player.position.x =
                    if (abs(gameState!!.player.position.x - 0f) < 0.0001f) 0f else gameState!!.player.position.x * scale.x

                gameState!!.player.position.y *= scale.y

                game?.pillars = pillars
                game?.player = gameState!!.player
            }
        }
    }

    public override fun onDraw(canvas: Canvas) {
        if (game == null) return

        game?.updateGame(timeDelta)

        game?.draw(canvas)
    }

    fun exit() {
        cacheHighScore()
        val newGame = Intent(context, MainMenu::class.java)
        this.context.startActivity(newGame)
    }

    fun cacheHighScore() {
        if (game == null) return

        val sharedPreferences = context.getSharedPreferences("com.example.mbkz_semestral_work", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val highest = sharedPreferences.getInt("score", 0)
        editor.putInt("score", if (highest < game!!.score) game!!.score else highest)
        editor.apply()
    }
}