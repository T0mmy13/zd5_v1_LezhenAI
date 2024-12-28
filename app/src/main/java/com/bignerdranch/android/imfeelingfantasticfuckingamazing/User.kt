package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val email: String,
    val password: String,
    val isTourAgent: Boolean,
    val isLoggedIn: Boolean = false
)