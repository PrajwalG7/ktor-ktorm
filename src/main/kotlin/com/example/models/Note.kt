package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    var id:Int,
    var note:String
)
