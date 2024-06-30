package com.example.mbkz_semestral_work.game

import java.io.Serializable

class GameState(
    val player: Player,
    val over: Boolean,
    val score: Int,
    val pillars: Serializable,
    val width: Float,
    val height: Float
) : Serializable
