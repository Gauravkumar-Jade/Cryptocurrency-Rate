package com.gaurav.cryptocurrencyrate.data

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.asLongState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import com.gaurav.cryptocurrencyrate.model.Crypto
import com.gaurav.cryptocurrencyrate.model.Data
import com.gaurav.cryptocurrencyrate.network.CryptoApiService
import com.gaurav.cryptocurrencyrate.util.AppDataStore
import com.gaurav.cryptocurrencyrate.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

interface CryptoRepository {

    /**
     * Get all crypto data from api or local storage
     */
     suspend fun getCryptos():Crypto

}

class DefaultCryptoRepository(
    private val context: Context,
    private val cryptoApiService: CryptoApiService,
    private val cryptoDao:CryptoDao,
    private val appDataStore: AppDataStore):CryptoRepository{


    override suspend fun getCryptos():Crypto {
      return withContext(Dispatchers.IO){
          if(NetworkUtils.isInternetAvailable(context)){
              val crypto = cryptoApiService.getCryptoRates()
              appDataStore.saveTimeStamp(crypto.timestamp)
              cryptoDao.removeData()
              cryptoDao.insert(crypto.data)
              crypto
          }else{
              val data = cryptoDao.getAllData()

              val timestamp = runBlocking {
                  appDataStore.getTimeStamp.first()
              }

              Crypto(data = data, timestamp = timestamp)
          }
      }
    }

}