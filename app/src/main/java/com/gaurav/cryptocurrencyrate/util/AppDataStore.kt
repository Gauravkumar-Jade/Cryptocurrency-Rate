package com.gaurav.cryptocurrencyrate.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AppDataStore(private val datastore: DataStore<Preferences>){

    companion object{
        val TIME_STAMP_KEY = longPreferencesKey("timeStamp")

    }

    suspend fun saveTimeStamp(timestamp:Long){
        datastore.edit {
            it[TIME_STAMP_KEY] = timestamp
        }
    }

    val getTimeStamp: Flow<Long> = datastore.data.map{
        it[TIME_STAMP_KEY]?:0
    }
}