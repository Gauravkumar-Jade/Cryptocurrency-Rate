package com.gaurav.cryptocurrencyrate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gaurav.cryptocurrencyrate.model.Data

@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class CryptoDatabase:RoomDatabase() {

    abstract fun getCryptoDao():CryptoDao

    companion object{
        private var INSTANCE:CryptoDatabase? = null

        fun getDatabase(context: Context):CryptoDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    CryptoDatabase::class.java,
                    "crypto_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}