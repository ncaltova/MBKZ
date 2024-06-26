package com.example.mbkz_semestral_work.utils

import android.view.MotionEvent
import java.util.LinkedList
import java.util.Queue
import kotlin.math.hypot

class InputProcessor {

    /**
     * Maximum limit of motion to be taken as click action
     */
    private val CLICK_MOTION_LIMIT = 3.0

    /**
     * Last registered motion event
     */
    private var lastInput : Input? = null

    /**
     * Queue holding all unprocessed events
     */
    private val inputQueue: Queue<MotionEvent> = LinkedList()

    /**
     * Add new input to the queue
     */
    fun addInput(event: MotionEvent) : Boolean {
        return inputQueue.offer(event)
    }

    /**
     * Return all pending inputs as iterable list
     */
    fun getPendingInput(): MutableList<Input>{
        var pending : Input? = this.getInput()

        val result : MutableList<Input> = mutableListOf()

        while (pending != null) {
            result.add(pending)
            pending = this.getInput()
        }

        return result
    }

    private fun getInput() : Input? {

        if (inputQueue.isEmpty()) return null

        val input = getEventType(inputQueue.remove())

        updateLastInput(input)

        return input
    }

    private fun getEventType(event : MotionEvent) : Input {
        return when(event.action) {
            MotionEvent.ACTION_DOWN -> Input(event.x, event.y, InputType.CLICK)
            MotionEvent.ACTION_UP ->
                if (lastInput != null && hypot(lastInput!!.x - event.x, lastInput!!.y - event.y) <= CLICK_MOTION_LIMIT)
                        Input(event.x, event.y, InputType.CLICK)
                else if (lastInput == null) Input(event.x, event.y, InputType.CLICK)
                else Input(event.x, event.y, InputType.LIFT)
            else -> Input(event.x, event.y, InputType.UNKNOWN)
        }
    }

    private fun updateLastInput(input: Input) {
        lastInput = when (input.type) {
            InputType.CLICK -> input
            else -> null
        }
    }
}

class Input (_x : Float, _y : Float, _type: InputType) {
    val x : Float = _x
    val y : Float = _y
    val type : InputType = _type
}

enum class InputType {
    CLICK, LIFT, UNKNOWN
}