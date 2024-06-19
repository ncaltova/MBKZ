package com.example.mbkz_semestral_work

import android.view.MotionEvent
import com.example.mbkz_semestral_work.utils.InputProcessor
import java.lang.Exception
import java.util.LinkedList
import java.util.Queue
import kotlin.math.roundToInt
import kotlin.random.Random

private const val DEFAULT_FPS = 30

class GameLoop(private val context: GameActivity) : Thread("GameLoopThread ${Random.nextInt()}") {

    private val view = GameView(context)

    var running: Boolean = false

    override fun run() {
        val fps = context.baseContext.display?.refreshRate
        val ticks = if (fps == null) 1000/DEFAULT_FPS else 1000/fps.roundToInt()

        while (running) {
            val start = System.currentTimeMillis()

            try {
                // TODO prekreslit hru
            } finally {
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