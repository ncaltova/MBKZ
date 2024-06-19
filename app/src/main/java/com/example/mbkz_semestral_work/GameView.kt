package com.example.mbkz_semestral_work

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.mbkz_semestral_work.utils.Input
import com.example.mbkz_semestral_work.utils.InputProcessor

class GameView(_context: Context) : SurfaceView(_context) {

    /**
     * Parent activity
     */
    private val context = _context

    /**
     * Input processor
     */
    private val inputProcessor = InputProcessor()

    /**
     * Game logic
     */
    private val game = Game()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        inputProcessor.addInput(event)
        return performClick()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun processPending(){
        var pending : Input? = inputProcessor.getInput()

        while (pending != null) {
            // Game process input

        }
    }
}