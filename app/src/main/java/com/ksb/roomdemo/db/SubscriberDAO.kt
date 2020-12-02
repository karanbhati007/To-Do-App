package com.ksb.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubs(subs:Subscriber) : Long

    @Update
    suspend fun updateSubs(subs : Subscriber)

    @Delete
    suspend fun deleteSubs(subs : Subscriber)

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAllSubs()

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubs():LiveData<List<Subscriber>>
}