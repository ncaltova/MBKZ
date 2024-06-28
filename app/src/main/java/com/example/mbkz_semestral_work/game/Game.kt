package com.example.mbkz_semestral_work.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.material3.Text
import com.example.mbkz_semestral_work.GameView
import com.example.mbkz_semestral_work.utils.InputProcessor
import com.example.mbkz_semestral_work.utils.InputType
import com.example.mbkz_semestral_work.utils.Position
import kotlin.math.log


class Game (
    private val gameView : GameView,
    private val inputProcessor : InputProcessor
) {

    /**
     * Parent view width
     */
    // init screen size
    private val screenWidth: Float = this.gameView.width.toFloat()

    /**
     * Parent view height
     */
    private val screenHeight: Float = this.gameView.height.toFloat()

    /**
     * Gravitational constant
     */
    private val gravity : Float = 0.005F

    /**
     * Instance of player
     */

    // init player
    private val player: Player = Player(Position(screenWidth/2, screenHeight/2))

    /**
     * Gap between pillars
     */
    private val pillarGap: Float = 800f

    /**
     * Speed for moving game over the x axis
     */
    private var gameSpeed : Float = 0.25F

    /**
     * Flag for game over
     */
    var over : Boolean = false

    /**
     * Game score
     */
    private var score: Int = 0

    /**
     * List of pillar in game
     */
    private val pillars : MutableList<Pillar> = mutableListOf()

    init {

        // init pillars
        var pillarX = 19*(screenWidth/20)

        for (i in 1..20) {
            val height = ((screenHeight/4).toInt()..(3*(screenHeight/4).toInt()))
                .random().toFloat()

            pillars.add(Pillar(Position(pillarX, screenHeight - height), 200f, height, false))

            pillarX += pillarGap
        }
    }

    /**
     * Updates player speed according to given timeDelta
     */
    private fun updatePlayerSpeedUp() {
        player.updateSpeedUp()
    }

    /**
     * Updates player speed according to given timeDelta
     */
    private fun updatePlayerSpeedDown(timeDelta: Float) {
        player.updateSpeedDown(timeDelta, gravity)
    }

    private fun movePillars(timeDelta: Float) {
        pillars.forEach{
            it.position.x -= timeDelta * this.gameSpeed

            if (!player.wasPlayerHit(it) && it.position.x < player.position.x && !it.visited) {
                score++
                it.visited = true
            }
        }

        // If first pillar is of screen -> add him at the end
        if (pillars.first().getBottomBoundingRect().right <= 0) {
            val shift = pillars.removeAt(0)

            val height = ((screenHeight/3).toInt()..(3*(screenHeight/4))
                .toInt()).random().toFloat()

            shift.position.x = pillars.last().position.x + pillarGap
            shift.position.y = screenHeight - height
            shift.height = height
            shift.visited = false

            pillars.add(shift)
        }
    }

    /**
     * Draws current state of game on screen
     */
    fun draw(canvas: Canvas)  {
        canvas.drawColor(Color.CYAN)

        val p = Paint()
        p.color = Color.RED
        canvas.drawCircle(player.position.x, player.position.y, player.getHitBox(), p)

        p.color = Color.GREEN
        this.pillars.forEach{
            canvas.drawRect(it.getTopBoundingRect(), p)
            canvas.drawRect(it.getBottomBoundingRect(), p)
        }

        p.color = Color.WHITE;
        p.textSize = 100f;
        val text = "Score: ${this.score}"
        val width = p.measureText(text)
        canvas.drawText(text, screenWidth/2 - width/2, 110f, p);

//        val d  = ContextCompat.getDrawable(this.gameView.context, R.drawable.pixel_cat)

//        d?.bounds = Rect(
//            player.position.x.toInt(),
//            player.position.y.toInt(),
//            (player.position.x+player.getHitBox()).toInt(),
//            (player.position.y+player.getHitBox()).toInt())
//
//        d?.draw(canvas)
    }

    /**
     * Updates game according to given time delta
     */
    fun updateGame(timeDelta: Float) : Pair<Boolean, Int>? {

        if (this.over) return null

        // Get all current inputs
        val input = inputProcessor.getPendingInput().filter {
            it.type == InputType.CLICK
        }

        // Move pillars
        this.movePillars(timeDelta)

        // Move player accordingly
        if (input.isNotEmpty()) {
            this.updatePlayerSpeedUp()

            input.forEach { _ ->
                this.player.movePlayer(timeDelta)

                if (this.wasPlayerHit()) {
                    val score = this.getScore()
                    this.clearScore()

                    return Pair(false, score)
                }
            }

            this.updatePlayerSpeedUp()
        } else {
            this.updatePlayerSpeedDown(timeDelta)

            this.player.movePlayer(timeDelta)

            if (this.wasPlayerHit()) {
                val score = this.getScore()
                this.clearScore()

                return Pair(false, score)
            }

            this.updatePlayerSpeedDown(timeDelta)
        }

        return Pair(true, this.getScore())
    }

    /**
     * Checks whether player was hit by any pillar
     */
    private fun wasPlayerHit() : Boolean {
        this.pillars.forEach {
            if (this.player.wasPlayerHit(it)) return true
        }

        return this.player.position.y + this.player.getHitBox() >= this.screenHeight ||
                this.player.position.y - this.player.getHitBox() <= 0f
    }

    /**
     * Clears game score
     */
    fun clearScore() {
        this.score = 0
    }

    /**
     * Returns current score
     */
    fun getScore() : Int {
        return this.score
    }
}