package br.com.fiap.esgame.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.esgame.R
import br.com.fiap.esgame.model.Endereco
import br.com.fiap.esgame.model.isValid
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BuscaCepSearchBar() {
    var cepState by remember { mutableStateOf("") }
    var listaCepsState by remember { mutableStateOf(listOf<Endereco>()) }
    var isError by remember { mutableStateOf(false) } // Indica se houve erro na busca
    val keyboardController = LocalSoftwareKeyboardController.current // Controlador do teclado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de pesquisa
        OutlinedTextField(
            value = cepState,
            onValueChange = {
                if (it.length <= 8) cepState = it
                isError = false
            },
            trailingIcon = {
                IconButton(onClick = {

                    keyboardController?.hide()

                    val call = RetrofitFactory().getCepService().getEndereco(cep = cepState)
                    call.enqueue(object : Callback<Endereco> {
                        override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                            if (response.isSuccessful && response.body() != null) {
                                listaCepsState = listOf(response.body()!!)
                                isError = listaCepsState.isEmpty()
                            }
                        }

                        override fun onFailure(call: Call<Endereco>, t: Throwable) {
                            isError = true
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("Pesquisar", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White,
                placeholderColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lógica para exibir a imagem ou os resultados
        when {
            cepState.isBlank() -> {
                // Exibe a imagem inicial quando o campo está vazio
                Image(
                    painter = painterResource(id = R.drawable.icone_gps),
                    contentDescription = "Imagem inicial",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(top = 80.dp)
                        .aspectRatio(1f) // Ajuste de tamanho
                )
            }
            isError -> {
                // Exibe imagem de erro se o CEP não for encontrado
                ScreenCepNaoEncontrado()
            }
            else -> {
                // Exibe a lista de resultados se houver endereços
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(listaCepsState) { endereco ->
                        if (endereco.isValid()) {
                            CardEndereco(endereco)
                        } else {
                            ScreenCepNaoEncontrado()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenCepNaoEncontrado() {
    Column(
        modifier = Modifier
            .fillMaxSize(), // Faz o Column ocupar toda a tela
        horizontalAlignment = Alignment.CenterHorizontally, // Centraliza os itens horizontalmente
        verticalArrangement = Arrangement.Center // Centraliza os itens verticalmente
    ) {
        Image(
            painter = painterResource(id = R.drawable.icone_nao_encontrado),
            contentDescription = "CEP não encontrado",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f) // Mantém a proporção da imagem
                .padding(top = 60.dp)
        )
        Text(
            text = "CEP não encontrado!",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}



@Composable
fun CardEndereco(endereco: Endereco) {
    Text(text = "Dados do CEP", color = Color.White, fontWeight = FontWeight.Bold)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp, top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00B140))
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
    Spacer(modifier = Modifier.height(16.dp))

    // Botão "Selecionar"
    Button(
        onClick = { TODO("Tem que repassar para a tela de Cadastro da Empresa que receberá os dados retornados")},
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B140))
    ) {
        Text("Selecionar", color = Color.White)
    }
}
