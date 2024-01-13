package com.gaurav.cryptocurrencyrate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CryptoData")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val ids:Int = 0,
    val name: String?,
    val priceUsd: String?,
    val symbol: String?,
    val changePercent24Hr: String?,
    val explorer: String?
)