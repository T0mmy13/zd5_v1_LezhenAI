package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "tourId"])
data class UserTourCrossRef(
    val userId: Int,
    val tourId: Int
)