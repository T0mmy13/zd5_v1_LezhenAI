package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TourDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTour(tour: Tour)

    @Delete
    suspend fun deleteTour(tour: Tour)

    @Transaction
    @Query("SELECT * FROM Tour WHERE id = :tourId")
    suspend fun getTourWithUsers(tourId: Int): List<TourWithUsers>

    @Query("SELECT * FROM Tour")
    suspend fun getAllTours(): List<Tour>

    @Query("SELECT * FROM Tour WHERE id = :tourId")
    suspend fun getTourById(tourId: Int): Tour?

    @Query("SELECT * FROM Tour WHERE registeredByAgentId = :agentId")
    suspend fun getToursByAgent(agentId: Int): List<Tour>
}