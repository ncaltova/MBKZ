package com.example.mbkz_semestral_work.game

import android.graphics.RectF
import com.example.mbkz_semestral_work.utils.Entity
import com.example.mbkz_semestral_work.utils.Position
import kotlin.math.pow
import kotlin.math.sqrt

class Player(

    /**
     * Current position of the player
     */
    val position: Position,

    ) {

    /**
     * Player hitbox (radius)
     */
    private val hitbox = 100f

    /**
     * Player speed
     */
    private val speed: Position = Position(0f, 0.05f)

    val boundingBox
        get() = RectF(
            position.x - hitbox,
            position.y - hitbox,
            position.x + hitbox,
            position.y + hitbox
        )

    /**
     * Calculates current player speed given current time delta.
     * Calculates change for only half of time delta for smoother animation.
     */
    fun updateSpeedDown(timeDelta: Float, gravityConst: Float) {
        speed.y += 0.5F * timeDelta * gravityConst
    }

    /**
     * Calculates current player speed given current time delta.
     * Calculates change for only half of time delta for smoother animation.
     */
    fun updateSpeedUp() {
        speed.y = -1.25f
    }

    /**
     * Move player according to the given time delta
     */
    fun movePlayer(timeDelta: Float) {
        this.position.x += timeDelta * this.speed.x
        this.position.y += timeDelta * this.speed.y
    }

    /**
     * Checks whether player hit pillar
     */
    fun wasPlayerHit(pillar: Pillar): Boolean {
        val bottomBoundingRect = pillar.getBottomBoundingRect()
        val topBoundingRect = pillar.getTopBoundingRect()

        return topBoundingRect.intersect(boundingBox)
                || bottomBoundingRect.intersect(boundingBox)

    }

    fun getHitBox(): Float {
        return hitbox
    }
}