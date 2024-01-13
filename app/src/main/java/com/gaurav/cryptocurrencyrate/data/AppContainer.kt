package com.gaurav.cryptocurrencyrate.data

import android.content.Context
import com.gaurav.cryptocurrencyrate.BuildConfig
import com.gaurav.cryptocurrencyrate.CryptoApplication
import com.gaurav.cryptocurrencyrate.network.CryptoApiService
import com.gaurav.cryptocurrencyrate.util.AppDataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val cryptoRepository:CryptoRepository
}

class DefaultAppContainer(private val context: Context):AppContainer{

    private val baseUrl = "https://api.coincap.io"

    private fun getClient() = if(BuildConfig.DEBUG){

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val apiService: CryptoApiService by lazy{
        retrofit.create(CryptoApiService::class.java)
    }


    override val cryptoRepository: CryptoRepository by lazy{
        DefaultCryptoRepository(
            context = context,
            cryptoApiService = apiService,
            cryptoDao = CryptoDatabase.getDatabase(context).getCryptoDao(),
            appDataStore = CryptoApplication.appDataStore
        )
    }
}