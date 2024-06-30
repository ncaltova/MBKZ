package com.example.mbkz_semestral_work.game

import android.graphics.RectF

import java.io.Serializable

class Pillar(
    val position: Position,
    private val width: Float,
    var height: Float,
    var visited: Boolean) : Serializable {

    private val verticalGap : Float = 600f

    private val positionY
        get() = if (position.y - verticalGap < 0f) 0f else position.y - verticalGap

    val bottomBoundingRect
        get() = RectF(
            position.x,
            position.y,
            position.x + width,
            position.y + height
        )

    val topBoundingRect
        get() = RectF(
            position.x,
            0f,
            position.x + width,
            positionY
        )
}