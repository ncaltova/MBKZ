package com.example.mbkz_semestral_work.utils

import android.view.MotionEvent
import java.util.LinkedList
import java.util.Queue
import kotlin.math.hypot

class InputProcessor {

    /**
     * Maximum limit of motion to be taken as click action
     */
    private val clickMotionLimit = 3.0

    /**
     * Last registered motion event
     */
    private var lastInput : Input? = null
        set(value) {
            field = when (value?.type) {
                InputType.CLICK -> input
                else -> null
            }
        }

    /**
     * Queue holding all unprocessed events
     */
    val inputQueue: Queue<MotionEvent> = LinkedList()

    private val input : Input?
        get() {
            if (inputQueue.isEmpty()) return null

            val input = getEventType(inputQueue.remove())

            lastInput = input

            return input
        }

    val pendingInput : MutableList<Input>
        get() {
            var pending = input

            val result : MutableList<Input> = mutableListOf()

            while (pending != null) {
                result.add(pending)
                pending = input
            }

            return result
        }

    private fun getEventType(event : MotionEvent) : Input {
        return when(event.action) {
            MotionEvent.ACTION_DOWN -> Input(event.x, event.y, InputType.CLICK)
            MotionEvent.ACTION_UP ->
                if (lastInput != null && hypot(lastInput!!.x - event.x, lastInput!!.y - event.y) <= clickMotionLimit)
                        Input(event.x, event.y, InputType.CLICK)
                else if (lastInput == null) Input(event.x, event.y, InputType.CLICK)
                else Input(event.x, event.y, InputType.LIFT)
            else -> Input(event.x, event.y, InputType.UNKNOWN)
        }
    }
}

class Input (
    val x : Float,
    val y : Float,
    val type: InputType
)

enum class InputType {
    CLICK, LIFT, UNKNOWN
}