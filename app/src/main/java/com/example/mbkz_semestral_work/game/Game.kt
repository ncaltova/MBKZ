package com.example.mbkz_semestral_work.game

import com.example.mbkz_semestral_work.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import com.example.mbkz_semestral_work.GameView
import com.example.mbkz_semestral_work.utils.Entity
import com.example.mbkz_semestral_work.utils.InputProcessor
import com.example.mbkz_semestral_work.utils.InputType
import com.example.mbkz_semestral_work.utils.Position
import kotlin.math.ln1p


class Game (
    private val gameView : GameView,
    private val inputProcessor : InputProcessor
) {

    /**
     * Parent view width
     */
    private val screenWidth: Float

    /**
     * Parent view height
     */
    private val screenHeight: Float

    /**
     * Gravitational constant
     */
    private val gravity : Float = 0.05F

    /**
     * Instance of player
     */
    private val player: Player

    /**
     * Instance of sky
     */
    private val sky: Entity

    /**
     * Instance of ground
     */
    private val ground: Entity

    /**
     * Gap between pillars
     */
    private val pillarGap: Float = 500f

    /**
     * Speed for moving game over the x axis
     */
    private var gameSpeed : Float = 0.05F

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
        // init screen size
        this.screenWidth = this.gameView.width.toFloat()
        this.screenHeight = this.gameView.height.toFloat()

        // init player
        this.player = Player(Position(screenWidth/2, screenHeight/2), 5.0F)

        // init game world
        this.sky = Entity(Position(screenWidth/2, 0.0F), 2.0F)
        this.ground = Entity(Position(screenWidth/2, screenHeight), 15.0F)

        // init pillars
        var pillarX = 3*(screenWidth/4)

        for (i in 1..20) {
            val height = ((screenHeight/4).toInt()..(3*(screenHeight/4).toInt()))
                .random().toFloat()

            pillars.add(Pillar(Position(pillarX, screenHeight - height), 200f, height))

            pillarX += pillarGap
        }
    }

    /**
     * Updates player speed according to given timeDelta
     */
    private fun updatePlayerSpeedUp(timeDelta: Float) {
        player.updateSpeedUp(timeDelta, sky, gravity)
    }

    /**
     * Updates player speed according to given timeDelta
     */
    private fun updatePlayerSpeedDown(timeDelta: Float) {
        player.updateSpeedDown(timeDelta, ground, gravity)
    }

    private fun movePillars(timeDelta: Float) {
        pillars.forEach{
            it.position.x -= timeDelta * this.gameSpeed

            if (!player.wasPlayerHit(it) && it.position.x < player.position.x) {
                score++
            }
        }

        // If frist pillar is of screen -> add him at the end
        if (pillars.first().getBottomBoundingRect().right <= 0) {
            val shift = pillars.removeAt(0)

            var height = ((screenHeight/3).toInt()..(3*(screenHeight/4))
                .toInt()).random().toFloat()

            shift.position.x = pillars.last().position.x + pillarGap
            shift.position.y = screenHeight - height
            shift.height = height

            pillars.add(shift)
        }
    }

    /**
     * Draws current state of game on screen
     */
    fun draw(canvas: Canvas)  {
        canvas.drawColor(Color.WHITE)

        val p = Paint()
        p.color = Color.RED
        canvas.drawCircle(player.position.x, player.position.y, player.getHitBox(), p)

        p.color = Color.GREEN
        this.pillars.forEach{
            canvas.drawRect(it.getTopBoundingRect(), p)
            canvas.drawRect(it.getBottomBoundingRect(), p)
        }

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
            this.updatePlayerSpeedUp(timeDelta)

            input.forEach { _ ->
                this.player.movePlayerUp(timeDelta)

                if (this.wasPlayerHit()) {
                    val score = this.getScore()
                    this.clearScore()

                    return Pair(false, score)
                }
            }

            this.updatePlayerSpeedUp(timeDelta)
        } else {
            this.updatePlayerSpeedDown(timeDelta)

            this.player.movePlayerDown(timeDelta)

            if (this.wasPlayerHit()) {
                val score = this.getScore()
                this.clearScore()

                return Pair(false, score)
            }

            this.updatePlayerSpeedUp(timeDelta)
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

        return this.player.position.y + this.player.getHitBox() >= this.ground.position.y ||
                this.player.position.y - this.player.getHitBox() <= this.sky.position.y
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