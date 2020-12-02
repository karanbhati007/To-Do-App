package com.ksb.roomdemo

import androidx.lifecycle.LiveData
import com.ksb.roomdemo.db.Subscriber
import com.ksb.roomdemo.db.SubscriberDAO

class SubscriberRepository(private val dao : SubscriberDAO) {

    val getAllSubs =  dao.getAllSubs()

    suspend fun insertSubs(subscriber: Subscriber) : Long{
       return dao.insertSubs(subscriber)
    }

    suspend fun updateSubs(subscriber: Subscriber){
        dao.updateSubs(subscriber)
    }

    suspend fun deleteSubs(subscriber: Subscriber){
        dao.deleteSubs(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAllSubs()
    }
}