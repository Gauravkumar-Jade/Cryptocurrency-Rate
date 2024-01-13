package com.gaurav.cryptocurrencyrate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.cryptocurrencyrate.model.Data

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<Data>)

    @Query("SELECT * FROM CryptoData")
    suspend fun getAllData():List<Data>

    @Query("DELETE FROM CryptoData")
    suspend fun removeData()
}