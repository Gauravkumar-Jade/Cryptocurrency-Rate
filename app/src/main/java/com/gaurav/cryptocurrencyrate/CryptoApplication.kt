package com.gaurav.cryptocurrencyrate

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gaurav.cryptocurrencyrate.data.AppContainer
import com.gaurav.cryptocurrencyrate.data.DefaultAppContainer
import com.gaurav.cryptocurrencyrate.util.AppDataStore

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "CryptoDatastore")

class CryptoApplication:Application() {

    lateinit var appContainer: AppContainer

    companion object{
        lateinit var appDataStore: AppDataStore
    }


    override fun onCreate() {
        super.onCreate()

        appContainer = DefaultAppContainer(this)
        appDataStore = AppDataStore(datastore)
    }
}