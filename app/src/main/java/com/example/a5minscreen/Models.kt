package com.example.a5minscreen

// liga o nome do JSON ("poster_path") ao nome Kotlin ("posterPath")
import com.google.gson.annotations.SerializedName

// Estado de um pedido à internet: a UI mostra spinner, erro ou dados conforme o caso
sealed class Estado<out T> {
    object Carregando : Estado<Nothing>()                        // pedido a decorrer
    data class Ok<T>(val dados: T) : Estado<T>()                 // pedido concluído, com os dados
    data class Erro(val mensagem: String) : Estado<Nothing>()    // pedido falhou
}

// Um filme tal como vem no JSON da TMDB
// O @SerializedName permite associar os nomes que vêm no JSON aos nomes utilizados no Kotlin.
data class FilmeApi(
    val id: Int,                                                 // "id"
    val title: String,                                           // "title"
    val overview: String?,                                       // "overview" (sinopse)
    @SerializedName("poster_path") val posterPath: String?,      // "poster_path": só "/abc.jpg", pode vir null
    @SerializedName("backdrop_path") val backdropPath: String?,  // "backdrop_path": imagem larga
    @SerializedName("release_date") val releaseDate: String?,    // "release_date": "ano-mes-dia"
    @SerializedName("vote_average") val voteAverage: Double?     // "vote_average": 8.5
)

// A resposta completa de uma lista: o JSON tem "results": [ {filme}, {filme}, ... ]
data class FilmesResponse(val results: List<FilmeApi>)
