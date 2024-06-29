package com.example.mbkz_semestral_work.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mbkz_semestral_work.GameView
import com.example.mbkz_semestral_work.R
import com.example.mbkz_semestral_work.game.Pillar
import com.example.mbkz_semestral_work.game.Player

class Draw (
    private val gameView: GameView
) {
    private val p = Paint()
    private val player_img = ContextCompat.getDrawable(this.gameView.context, R.drawable.vector_cat)
    private val game_typeface = ResourcesCompat.getFont(gameView.context, R.font.flappy_font)

    fun drawPartialState(
        text : String,
        canvas: Canvas,
        screenWidth: Float,
        screenHeight: Float
    ) {
        p.typeface = game_typeface
        p.color = Color.WHITE
        canvas.drawColor(Color.parseColor("#32000000"))
        p.textSize = 200f
        canvas.drawText(text, screenWidth / 2 - p.measureText(text) / 2, screenHeight / 2 - 100f, p)
    }

    fun drawPlayer(player: Player, canvas: Canvas) {
        player_img?.bounds = player.drawBox
        player_img?.draw(canvas)
    }

    fun drawPillars(
        pillars: MutableList<Pillar>,
        canvas : Canvas,
        screenWidth: Float,
    ) {
        p.color = Color.parseColor("#71D74D")

        pillars.forEach{
            if (it.position.x > screenWidth)

            p.color = Color.parseColor("#71D74D")
            canvas.drawRect(it.topBoundingRect, p)
            canvas.drawRect(it.bottomBoundingRect, p)
        }
    }
}