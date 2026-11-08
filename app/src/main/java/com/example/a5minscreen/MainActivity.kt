package com.example.a5minscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.a5minscreen.ui.theme._5minScreenTheme

import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {                   // a Activity: ponto de entrada da app
    override fun onCreate(savedInstanceState: Bundle?) {     // corre quando a app abre
        super.onCreate(savedInstanceState)                   // obrigatório: deixa o Android fazer a sua parte
        setContent {                                         // "aqui dentro está o interface em Compose"
            _5minScreenTheme {                               // o tema gerado pelo projeto (Theme.kt)
                MainProgram()                                // o nosso ecrã principal
            }
        }
    }
}

@Composable
fun MainProgram() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, Destination.toList) }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            AppNavigation(navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Destination.Home.route) {   // começa na Home
        composable(Destination.Home.route)     { HomeScreen() }           // rota "home" → HomeScreen
        composable(Destination.Search.route)   { SearchScreen() }
        composable(Destination.Activity.route) { ActivityScreen() }
        composable(Destination.Profile.route)  { ProfileScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, appItems: List<Destination>) {
    BottomNavigation(backgroundColor = Color(0xFF14181C), contentColor = Color.White) {
        val backStackEntry by navController.currentBackStackEntryAsState()  //em que ecrã está
        val currentRoute = backStackEntry?.destination?.route               // rota ativa (pode ser null)
        appItems.forEach { item ->                                          // repete para cada aba
            val selected = currentRoute == item.route                       // esta aba está ativa?
            val tint = if (selected) Color.White else Color.White.copy(0.4f) // ativa = branco; inativa = blur
            BottomNavigationItem(
                icon = { Icon(painterResource(item.icon), item.title, tint = tint) },  // ícone (item.title = descrição para leitores de ecrã)
                label = { Text(item.title, fontSize = 12.sp, color = tint) },          // texto por baixo do ícone
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }  // volta à Home e guarda o estado de quem sai
                        }
                        launchSingleTop = true   // não empilha cópias do mesmo ecrã
                        restoreState = true      // recupera o estado guardado da aba
                    }
                }
            )
        }
    }
}