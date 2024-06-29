package com.example.mbkz_semestral_work.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.graphics.Brush
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mbkz_semestral_work.GameView
import com.example.mbkz_semestral_work.R
import com.example.mbkz_semestral_work.utils.Draw
import com.example.mbkz_semestral_work.utils.GameState
import com.example.mbkz_semestral_work.utils.InputProcessor
import com.example.mbkz_semestral_work.utils.InputType
import com.example.mbkz_semestral_work.utils.Position
import java.io.Serializable


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
    var player: Player = Player(
        Position(screenWidth/2, screenHeight/2)
    )

    /**
     * Gap between pillars
     */
    private val pillarGap: Float = 800f

    /**
     * Speed for moving game over the x axis
     */
    private val gameSpeed : Float = 0.25F

    /**
     * Draws current state od game
     */
    private val draw : Draw = Draw(this.gameView)

    /**
     * Returns current state of game
     */
    val gameState
        get() = GameState(
            player,
            over,
            score,
            pillars as Serializable,
            screenWidth,
            screenHeight
        )

    /**
     * Flag for game over
     */
    var over : Boolean = false

    /**
     * Flag for game pause
     */
    var pause : Boolean = false

    /**
     * Game score
     */
    var score: Int = 0

    /**
     * List of pillar in game
     */
    var pillars : MutableList<Pillar> = mutableListOf()



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
        if (pillars.first().bottomBoundingRect.right <= 0) {
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
        draw.drawBackground(canvas)

        draw.drawPlayer(player, canvas)

        draw.drawPillars(pillars, canvas, screenWidth)

        draw.drawScore(score, canvas)

        if (over)
            draw.drawPartialState("GAME OVER", canvas, screenWidth, screenHeight)

        if (pause)
            draw.drawPartialState("PAUSE", canvas, screenWidth, screenHeight)
    }

    /**
     * Updates game according to given time delta
     */
    fun updateGame(timeDelta: Float) : Pair<Boolean, Int>? {

        // Get all current inputs
        val input = inputProcessor.pendingInput.filter {
            it.type == InputType.CLICK
        }

        if (over && input.isNotEmpty()) {
            gameView.exit()
            score = 0
            return null
        }
        else if (pause && input.isNotEmpty()) {
            pause = false
        } else if (over || pause) {
            return null
        }

        // Move pillars
        movePillars(timeDelta)

        // Move player accordingly
        if (input.isNotEmpty()) {
            updatePlayerSpeedUp()

            input.forEach { _ ->
                player.movePlayer(timeDelta)

                if (wasPlayerHit()) {
                    over = true
                    return Pair(false, score)
                }
            }

            updatePlayerSpeedUp()
        } else {
            updatePlayerSpeedDown(timeDelta)

            player.movePlayer(timeDelta)

            if (wasPlayerHit()) {
                over = true
                return Pair(false, score)
            }

            updatePlayerSpeedDown(timeDelta)
        }

        return Pair(true, score)
    }

    /**
     * Checks whether player was hit by any pillar
     */
    private fun wasPlayerHit() : Boolean {
        pillars.forEach {
            if (player.wasPlayerHit(it)) return true
        }

        return player.position.y + player.hitbox >= screenHeight ||
                player.position.y - player.hitbox <= 0f
    }
}