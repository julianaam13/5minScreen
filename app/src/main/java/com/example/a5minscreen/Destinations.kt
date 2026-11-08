package com.example.a5minscreen
sealed class Destination(val route: String, val icon: Int, val title: String) {  // molde de um ecrã: rota, ícone, texto
    object Home     : Destination("home",     R.drawable.baseline_home_24,   "Home")       // aba 1
    object Search   : Destination("search",   R.drawable.baseline_search_24, "Pesquisa")   // aba 2
    object Activity : Destination("activity", R.drawable.outline_checklist_24,   "Atividade")  // aba 3
    object Profile  : Destination("profile",  R.drawable.baseline_person_24, "Perfil")     // aba 4

    companion object {
        val toList = listOf(Home, Search, Activity, Profile)  // lista que a barra percorre para desenhar os botões
    }
}