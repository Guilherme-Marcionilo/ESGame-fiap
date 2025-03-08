package br.com.fiap.esgame.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.esgame.model.Endereco
import br.com.fiap.esgame.ui.theme.BluePrimary
import br.com.fiap.esgame.service.RetrofitFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaCepScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuScreen { scope.launch { drawerState.close() } } // Usa o menu separado
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BluePrimary)) {
            BuscaCepTopBar { scope.launch { drawerState.open() } }
            BuscaCepSearchBar()
        }
    }
}


@Composable
fun BuscaCepTopBar(onMenuClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BluePrimary)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Busca CEP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Busca CEP", tint = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaCepSearchBar() {
    var cepState by remember { mutableStateOf("") }
    var listaCepsState by remember {
        mutableStateOf(listOf<Endereco>())
    }

    OutlinedTextField(
        value = cepState,
        onValueChange = {
            Log.i("FIAP", "onResponse: $cepState")
            cepState = it
        },
        trailingIcon = {
            IconButton(onClick = {
                var call = RetrofitFactory().getCepService().getEndereco(cep = cepState)
                call.enqueue(object : Callback<Endereco> {
                    override fun onResponse(
                        call: Call<Endereco>,
                        response: Response<Endereco>
                    ) {
                        Log.i("FIAP", "onResponse: ${response.body()}")
                        response.body()?.let { endereco ->
                            listaCepsState = listaCepsState + endereco
                        }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Log.e("FIAP", "Erro na requisição: ${t.message}", t)
                    }


                })
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.White,
                    contentDescription = ""
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        placeholder = { Text("Pesquisar", color = Color.White) },
        textStyle = TextStyle(color = Color.White), // Define a cor do texto digitado
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn() {
        items(listaCepsState) {
            CardEndereco(it)
        }
    }
}

@Composable
fun CardEndereco(endereco: Endereco) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00B140)) // Define a cor de fundo
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "CEP: ${endereco.cep}", color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "Rua: ${endereco.rua}", color = Color.White)
            Text(text = "Cidade: ${endereco.cidade}", color = Color.White)
            Text(text = "Bairro: ${endereco.bairro}", color = Color.White)
            Text(text = "UF: ${endereco.uf}", color = Color.White)
        }
    }
}
