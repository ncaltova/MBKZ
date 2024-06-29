package com.example.mbkz_semestral_work

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.PixelFormat
import java.lang.Exception
import kotlin.math.roundToInt
import kotlin.random.Random

private const val DEFAULT_FPS = 30

class GameLoop(private val view: GameView) : Thread("GameLoopThread ${Random.nextInt()}") {

    var running: Boolean = false

    private var time: Long = 0

    @SuppressLint("WrongCall")
    override fun run() {
        val fps = view.context.display?.refreshRate
        val ticks = if (fps == null) 1000/DEFAULT_FPS else 1000/fps.roundToInt()

        var c: Canvas? = null

        time = System.currentTimeMillis()

        synchronized(view.holder) {
            view.holder.setFormat(PixelFormat.RGB_565)
        }

        while (running) {
            val start = System.currentTimeMillis()

            try {
                c = view.holder.lockCanvas()

                synchronized(view.holder) {
                    if (c != null) {
                        view.timeDelta = (System.currentTimeMillis() - time).toFloat()
                        time = System.currentTimeMillis()
                        view.onDraw(c)
                    }
                }

            } finally {
                if (c != null) {
                    view.holder.unlockCanvasAndPost(c)
                }
            }

            var sleep = ticks - (System.currentTimeMillis() - start)
            sleep = if (sleep > 0) sleep else 10

            try {
                sleep(sleep)
            } catch (_: Exception) {
            }
        }
    }
}