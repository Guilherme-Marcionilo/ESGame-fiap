package br.com.fiap.esgame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.esgame.R
import br.com.fiap.esgame.ui.theme.BluePrimary

@Composable
fun MenuScreen(onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(BluePrimary)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onItemClick) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Imagem do usuário",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nome Sobrenome",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(32.dp)
                        .width(100.dp)
                ) {
                    Text("Ver perfil", color = Color.White, fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        DrawerItem("Início", R.drawable.icone_inicio, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Ranking", R.drawable.icone_ranking, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Missões", R.drawable.icone_missao, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Conquistas", R.drawable.icone_conquista, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Recompensas/Loja", R.drawable.icone_recompensa, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Configurações", R.drawable.icone_configuracao, onItemClick)
        Spacer(modifier = Modifier.height(6.dp))
        DrawerItem("Sair", R.drawable.icone_sair, onItemClick)
        Spacer(modifier = Modifier.weight(2f))
        Image(
            painter = painterResource(id = R.drawable.logo_esgame),
            contentDescription = "Logo ESGame",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerItem(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp, color = Color.White)
    }
}

