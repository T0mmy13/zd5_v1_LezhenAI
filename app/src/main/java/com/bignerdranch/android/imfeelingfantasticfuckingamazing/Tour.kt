package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tour(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val description: String,
    val cost: Double,
    val image: String = "Start_icon.png",
    val registeredByAgentId: Int
)