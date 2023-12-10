package com.example.tee_together

import java.io.Serializable

data class HoleData(var score: Int, var par: Int, var fir: Boolean = false, var gir: Boolean = false) : Serializable
