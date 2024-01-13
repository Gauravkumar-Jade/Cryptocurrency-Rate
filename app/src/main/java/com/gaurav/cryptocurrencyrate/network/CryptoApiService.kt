package com.gaurav.cryptocurrencyrate.network

import com.gaurav.cryptocurrencyrate.model.Crypto
import retrofit2.http.GET

interface CryptoApiService {

    @GET("/v2/assets")
    suspend fun getCryptoRates():Crypto
}