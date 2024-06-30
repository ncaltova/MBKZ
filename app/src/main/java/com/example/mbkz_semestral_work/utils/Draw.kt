package com.example.mbkz_semestral_work.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.mbkz_semestral_work.game.GameView
import com.example.mbkz_semestral_work.R
import com.example.mbkz_semestral_work.game.Pillar
import com.example.mbkz_semestral_work.game.Player

class Draw(
    private val gameView: GameView
) {
    private val p = Paint()
    private val player_img = ContextCompat.getDrawable(this.gameView.context, R.drawable.cat)
    private val game_typeface = ResourcesCompat.getFont(gameView.context, R.font.flappy_font)

    private var gradientBitmap: Bitmap? = null

    fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        val oldGradientBitmap = gradientBitmap


        val newGradientDrawable =
            if (oldGradientBitmap == null ||
                oldGradientBitmap.width != canvas.width ||
                oldGradientBitmap.height != canvas.height
            ) GradientDrawable()
                .apply {
                    colors = intArrayOf(
                        0xffbdf8ff.toInt(),
                        0xFFffffff.toInt(),
                        0xFFb8ff66.toInt(),
                    )
                    gradientType = GradientDrawable.LINEAR_GRADIENT
                    bounds = Rect(0, 0, canvas.width, canvas.height)
                    orientation = GradientDrawable.Orientation.TOP_BOTTOM
                }
                .toBitmap(canvas.width, canvas.height, Bitmap.Config.RGB_565)
                .also { gradientBitmap = it }
            else oldGradientBitmap


        canvas.drawBitmap(newGradientDrawable, 0f, 0f, null)
    }

    fun drawPartialState(
        text: String,
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
        canvas: Canvas,
        screenWidth: Float,
    ) {
        p.color = Color.parseColor("#96fa23")


        pillars.forEach {
            if (it.position.x > screenWidth) return

            p.color = Color.parseColor("#96fa23")
            canvas.drawRect(it.topBoundingRect, p)
            canvas.drawRect(it.bottomBoundingRect, p)
        }
    }

    fun drawScore(score: Int, canvas: Canvas) {
        p.typeface = ResourcesCompat.getFont(gameView.context, R.font.flappy_font)
        p.color = Color.WHITE
        p.textSize = 150f
        val text = "$score"
        val width = p.measureText(text)
        canvas.drawText(text, gameView.width/2 - width/2, 110f, p)
    }
}