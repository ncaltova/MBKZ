package com.example.mbkz_semestral_work.game

import android.graphics.Rect
import android.graphics.RectF
import com.example.mbkz_semestral_work.utils.Position
import java.io.Serializable

class Player(

    /**
     * Current position of the player
     */
    val position: Position,

    ) : Serializable {

    /**
     * Player hitbox (radius), little bit less than visual size
     */
    val hitbox = 92f

    /**
     * Player visual size
     */
    private val size = 100f

    /**
     * Player speed
     */
    private val speed: Position = Position(0f, 0.05f)

    val drawBox
        get() = Rect(
        (position.x - size).toInt(),
        (position.y - size).toInt(),
        (position.x + size).toInt(),
        (position.y + size).toInt()
    )

    private val boundingBox
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
        this.position.y += timeDelta * this.speed.y
    }

    /**
     * Checks whether player hit pillar
     */
    fun wasPlayerHit(pillar: Pillar): Boolean {
        return pillar.topBoundingRect.intersect(boundingBox)
                || pillar.bottomBoundingRect.intersect(boundingBox)

    }
}