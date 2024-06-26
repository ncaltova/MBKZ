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

    /**
     * Player mass
     */
    private val mass: Float,

    ) {
    /**
     * Player hitbox (radius)
     */
    private val hitbox = 100f

    /**
     * Player speed up
     */
    private val speedUp: Position = Position(0f, 5f)

    /**
     * Player speed down
     */
    private val speedDown: Position = Position(0f, 0.05f)

    val boundingBox
        get() = RectF(
            position.x - hitbox,
            position.y - hitbox,
            position.x + hitbox,
            position.y + hitbox
        )

    /**
     * Calculates player acceleration based on base entity
     */
    private fun getAcceleration(base: Entity, gravityConst: Float): Position {
        val distanceX: Float = base.position.x - this.position.x
        val distanceY: Float = base.position.y - this.position.y

        val distance = sqrt(distanceX.pow(2) + distanceY.pow(2))

        return Position(
            (gravityConst * this.mass * base.mass * distanceX) / distance.pow(3),
            (gravityConst * this.mass * base.mass * distanceY) / distance.pow(3)
        )
    }

    /**
     * Calculates current player speed given current time delta.
     * Calculates change for only half of time delta for smoother animation.
     */
    fun updateSpeedDown(timeDelta: Float, base: Entity, gravityConst: Float) {
        val acceleration = getAcceleration(base, gravityConst)

        speedDown.x += 0.5F * timeDelta * acceleration.x
        speedDown.y += 0.5F * timeDelta * acceleration.y
    }

    /**
     * Calculates current player speed given current time delta.
     * Calculates change for only half of time delta for smoother animation.
     */
    fun updateSpeedUp(timeDelta: Float, base: Entity, gravityConst: Float) {
        val acceleration = getAcceleration(base, gravityConst)

        speedDown.x -= 0.5F * timeDelta * acceleration.x
        speedDown.y -= 0.5F * timeDelta * acceleration.y
    }

    /**
     * Move player according to the given base entity
     */
    fun movePlayerDown(timeDelta: Float) {
        this.position.x += timeDelta * this.speedDown.x
        this.position.y += 2 * timeDelta * this.speedDown.y
    }

    /**
     * Move player according to the given base entity
     */
    fun movePlayerUp(timeDelta: Float) {
        this.position.x -= timeDelta * this.speedUp.x
        this.position.y -= timeDelta * this.speedUp.y
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