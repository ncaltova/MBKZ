package com.example.mbkz_semestral_work.game

import android.graphics.RectF

import com.example.mbkz_semestral_work.utils.Position

class Pillar(val position: Position, private val width: Float, var height: Float) {
    private val verticalGap : Float = 400f

    fun getBottomBoundingRect() = RectF(
        position.x, position.y, position.x + width, position.y + height
    )

    fun getTopBoundingRect() : RectF{
        val posY = if (position.y - verticalGap < 0f) 0f else position.y - verticalGap
        return RectF(
            position.x, 0f, position.x + width, posY
        )
    }
}