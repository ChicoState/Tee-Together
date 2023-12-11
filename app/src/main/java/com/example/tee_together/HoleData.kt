package com.example.tee_together

import java.io.Serializable
// Edit this to allow for multiple users
data class UserHoleData(
    var score: Int,
    var par: Int,
    var fir: Boolean = false,
    var gir: Boolean = false
) : Serializable

// HoleData.kt
data class HoleData(
    var userScores: MutableMap<String, UserHoleData>
) : Serializable
