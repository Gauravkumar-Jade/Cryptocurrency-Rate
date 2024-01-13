package com.gaurav.cryptocurrencyrate.ui.uistate

import com.gaurav.cryptocurrencyrate.model.Data

sealed interface CryptoUiState {
    object Loading:CryptoUiState
    data class Success(val data:List<Data>, val timeStamp:Long):CryptoUiState
    data class HttpError(val code:Int?, val message: String?): CryptoUiState
    data class Error(val message: String?): CryptoUiState
}