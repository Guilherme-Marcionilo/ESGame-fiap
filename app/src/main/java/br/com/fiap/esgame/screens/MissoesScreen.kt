package br.com.fiap.esgame.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.esgame.ui.theme.BluePrimary
import br.com.fiap.esgame.ui.theme.GreenHighlight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissoesScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuScreen { scope.launch { drawerState.close() } } // Usa o menu separado
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BluePrimary)) {
            TopBar { scope.launch { drawerState.open() } }
            SearchBar()
            MissionSection("Missões em vigor:", listOf("Reciclagem" to "1 a 31/03/2025", "Agasalho" to "1 a 31/03/2025"))
            MissionSection("Missões encerradas:", listOf("Reciclagem" to "1 a 28/02/2025", "Reciclagem" to "1 a 31/01/2025"), isCompleted = true)
        }
    }
}


@Composable
fun TopBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BluePrimary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Missões",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "", onValueChange = {},
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Pesquisar") },
        placeholder = { Text("Pesquisar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun MissionSection(
    title: String,
    missions: List<Pair<String, String>>,
    isCompleted: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GreenHighlight)
        Spacer(modifier = Modifier.height(8.dp))
        missions.forEach { mission -> MissionCard(mission.first, mission.second, isCompleted) }
    }
}

@Composable
fun MissionCard(name: String, period: String, isCompleted: Boolean) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = "Vigência: $period", fontSize = 14.sp, color = Color.Gray)
            if (isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Seu setor ficou em 1º lugar! 🏆", fontWeight = FontWeight.Bold)
                Text(
                    text = "Você subiu 15 posições desde a última missão.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
