package com.example.a5minscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() = Placeholder("Home")

@Composable
fun SearchScreen() = Placeholder("Pesquisa")

@Composable
fun ActivityScreen() = Placeholder("Atividade")

@Composable
fun ProfileScreen() = Placeholder("Perfil")

@Composable
private fun Placeholder(text: String) { // função auxiliar, evita repetir código
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}