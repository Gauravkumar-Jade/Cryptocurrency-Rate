package com.gaurav.cryptocurrencyrate.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaurav.cryptocurrencyrate.R
import com.gaurav.cryptocurrencyrate.model.Data
import com.gaurav.cryptocurrencyrate.ui.uistate.CryptoUiState
import com.gaurav.cryptocurrencyrate.util.NetworkUtils


@Composable
fun HomeScreen(uiState: CryptoUiState,getTimeStamp:(time:Long) -> Unit, modifier: Modifier = Modifier){


    when(uiState){
        is CryptoUiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())}
        is CryptoUiState.Success -> {
            getTimeStamp.invoke(uiState.timeStamp)
            CryptoRateListScreen(data = uiState.data, modifier = Modifier.fillMaxSize())
        }

        is CryptoUiState.Error -> {
            ErrorScreen(code = null, message = uiState.message)
        }

        is CryptoUiState.HttpError -> {
            ErrorScreen(code = uiState.code.toString(), message = uiState.message)
        }
    }

}


@Composable
fun CryptoRateListScreen(data: List<Data>, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ){
        items(data){
            CryptoCard(data = it,
                modifier = Modifier.padding(8.dp))
        }
    }

}


@Composable
fun CryptoCard(data: Data, modifier: Modifier = Modifier){

    val priceChange = data.changePercent24Hr
    val price = String.format("%.3f", data.priceUsd?.toDouble())

    Card(modifier = modifier
    ) {
        Column(modifier = Modifier
            .padding(8.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = data.symbol.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,

                )
                Text(
                    text = stringResource(R.string.dollar_price, price),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = data.name.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                    )

                when{
                    priceChange?.toDouble()!! > 0 -> {
                        PriceChangeWithArrow(
                            priceChange = priceChange,
                            arrowIcon = Icons.Filled.ArrowUpward,
                            color = colorResource(id = R.color.green)
                        )
                    }
                    priceChange.toDouble() < 0 -> {
                        PriceChangeWithArrow(
                            priceChange = priceChange,
                            arrowIcon = Icons.Filled.ArrowDownward,
                            color = colorResource(id = R.color.red)
                        )
                    }
                    else -> {
                        PriceChangeWithArrow(
                            priceChange = priceChange,
                            arrowIcon = Icons.Filled.SwapVert,
                            color = colorResource(id = R.color.black)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun PriceChangeWithArrow(
    priceChange: String,
    arrowIcon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color
){
    val priceChanged = String.format("%.2f", priceChange.toDouble())

    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {

        Icon(imageVector = arrowIcon,
            tint = color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "$priceChanged %",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}



@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}


@Composable
fun ErrorScreen(code: String?,
                message: String?,
                modifier: Modifier = Modifier
) {
    val errorMessage = "$code-$message"
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = errorMessage, modifier = Modifier.padding(16.dp))
    }
}



@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun HomeScreenPreview(){


}