package com.gaurav.cryptocurrencyrate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gaurav.cryptocurrencyrate.CryptoApplication
import com.gaurav.cryptocurrencyrate.data.CryptoRepository
import com.gaurav.cryptocurrencyrate.ui.uistate.CryptoUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CryptoViewModel(private val repository: CryptoRepository):ViewModel() {

    var cryptoUiState: CryptoUiState by mutableStateOf(CryptoUiState.Loading)
        private set


    init{
        getCryptoRates()
    }

    fun getCryptoRates() {

        viewModelScope.launch {
            cryptoUiState = CryptoUiState.Loading

            cryptoUiState = try{
                val crypto = repository.getCryptos()
                if(crypto.data.isNotEmpty() && crypto.timestamp != 0L)
                    CryptoUiState.Success(data = crypto.data, timeStamp = crypto.timestamp)
                else
                    CryptoUiState.Error(message = "No local data available")
            }catch (e:Exception){
                CryptoUiState.Error(message = e.message)
            }
            catch (e:HttpException){
                CryptoUiState.HttpError(code=e.code(),message = e.message)
            }
            catch (e:IOException){
                CryptoUiState.Error(message = e.message)
            }
        }

    }

    companion object{
        val factory:ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CryptoApplication)
                val repository = application.appContainer.cryptoRepository
                CryptoViewModel(repository = repository)
            }
        }
    }
}
