package com.gaurav.cryptocurrencyrate.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaurav.cryptocurrencyrate.R
import com.gaurav.cryptocurrencyrate.ui.screen.HomeScreen
import com.gaurav.cryptocurrencyrate.ui.theme.CryptocurrencyRateTheme
import com.gaurav.cryptocurrencyrate.ui.viewmodel.CryptoViewModel
import com.gaurav.cryptocurrencyrate.util.NetworkUtils
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoApp(){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var timestamp by remember {
        mutableLongStateOf(0L)
    }

    val viewModel: CryptoViewModel  = viewModel(factory = CryptoViewModel.factory)

    val context = LocalContext.current
    showNoNetworkMessage(context = context)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CryptoAppTopBar(onRefresh = {
            viewModel.getCryptoRates()
            showNoNetworkMessage(context = context)
        },
            scrollBehavior = scrollBehavior,
            timestamp) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            HomeScreen(
                uiState = viewModel.cryptoUiState,
                getTimeStamp =  {time -> timestamp = time}
            )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAppTopBar(onRefresh:()->Unit, scrollBehavior: TopAppBarScrollBehavior, timestamp:Long,modifier: Modifier = Modifier){

    Row(modifier = modifier
        .fillMaxWidth()
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.crypto_rates),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold)
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { onRefresh.invoke() }) {
                        Icon(imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.refresh),
                            modifier = Modifier.size(30.dp))
                    }
                },
                modifier = modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)){
                Text(text = getSimpleTime(timestamp, LocalContext.current.applicationContext),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium)
            }

        }
    }
}

private fun getSimpleTime(timestamp: Long, context: Context):String{
    return if(timestamp != 0L){
        val sdf = SimpleDateFormat("dd/MM/yy, hh:mm a")
        val netDate = Date(timestamp)
        context.getString(R.string.last_update, sdf.format(netDate))
    }else{
        ""
    }
}

private fun showNoNetworkMessage(context:Context){
    if(!NetworkUtils.isInternetAvailable(context)){
        Toast.makeText(context, context.getString(R.string.no_connection_message), Toast.LENGTH_LONG)
            .show()
    }
}

@Preview(showSystemUi = true)
@Composable
fun CryptoAppPreview(){
    CryptocurrencyRateTheme {
        CryptoApp()
    }
}