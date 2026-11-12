package com.example.a5minscreen
// Constantes da app: endereço da API, chave e construção dos URLs das imagens

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"      // endereço base da TMDB (termina sempre em "/")
    const val API_KEY = "90e68d2bbb0aa8914879f91a59aeb289"             // <<< A CHAVE v3 (32 caracteres) VAI SÓ AQUI
    const val IDIOMA = "pt-PT"

    private const val IMG = "https://image.tmdb.org/t/p/"      // a TMDB devolve só "/abc.jpg": é preciso juntar isto à frente
    fun poster(path: String?): String? = path?.let { "${IMG}w342$it" }   // capa de tamanho médio
    //fun fundo(path: String?): String? = path?.let { "${IMG}w780$it" }    // imagem larga (backdrop)
    //fun perfil(path: String?): String? = path?.let { "${IMG}w185$it" }   // foto de ator

}
