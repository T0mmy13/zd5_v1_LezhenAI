package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {

    @Query("DELETE FROM UserTourCrossRef WHERE userId = :userId AND tourId = :tourId")
    suspend fun deleteUserTourCrossRef(userId: Int, tourId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserTourCrossRef(crossRef: UserTourCrossRef)

    @Query("SELECT * FROM User WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): User?

    @Transaction
    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserWithTours(userId: Int): UserWithTours?

    @Query("UPDATE User SET isLoggedIn = 0")
    suspend fun clearCurrentLoggedInUser()

    @Query("UPDATE User SET isLoggedIn = 1 WHERE login = :login")
    suspend fun setCurrentLoggedInUser(login: String)

    @Query("SELECT * FROM User WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentLoggedInUser(): User?
}