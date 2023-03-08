package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.CryptoModel
import com.example.myapplication.service.CryptoAPI
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    var cryptoModels = remember {
        mutableStateListOf<CryptoModel>()
    }
    val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()
    call.enqueue(object : Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    //todo: this area will show data list
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }
    })

    Scaffold(topBar = { AppBar() }) {
        CryptoList(cryptos = cryptoModels)
    }

}

@Composable
fun AppBar() {

    TopAppBar(contentPadding = PaddingValues(10.dp), backgroundColor = Color.Green) {
        Text(
            text = "Retrofit Compose",
            fontSize = 24.sp,
            color = Color.Black,
            fontStyle = FontStyle.Normal,
        )
    }
}

@Composable
fun CryptoList(cryptos: List<CryptoModel>) {

    LazyColumn(contentPadding = PaddingValues(3.dp)) {
        items(cryptos) { crypto ->
            CryptoRow(crypto = crypto)

        }
    }
}

@Composable
fun CryptoRow(crypto: CryptoModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .border(2.dp, color = Color.Black)
    ) {
        Text(
            text = crypto.currency,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(3.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = crypto.price,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(3.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}