package com.example.a5minscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ViewModel da Home: vai buscar os filmes à TMDB e guarda-os. Sobrevive a rotações do ecrã.
class HomeViewModel : ViewModel() {
    private val api = TmdbApi.service                             // o Retrofit criado no TmdbService.kt

    // Cada secção da Home tem o seu estado (começa em "A carregar")
    val populares = MutableStateFlow<Estado<List<FilmeApi>>>(Estado.Carregando)
    val melhores = MutableStateFlow<Estado<List<FilmeApi>>>(Estado.Carregando)
    val emCartaz = MutableStateFlow<Estado<List<FilmeApi>>>(Estado.Carregando)
    val brevemente = MutableStateFlow<Estado<List<FilmeApi>>>(Estado.Carregando)

    init { recarregar() }       // ao criar o ViewModel, já pede os dados

    fun recarregar() {                                     // também é chamada pelo botão "Tentar de novo"
        carregar(populares) { api.populares().results }     // GET movie/popular  -> lista de filmes
        carregar(melhores) { api.melhores().results }       // GET movie/top_rated
        carregar(emCartaz) { api.emCartaz().results }       // GET movie/now_playing
        carregar(brevemente) { api.brevemente().results }   // GET movie/upcoming
    }

    // Função genérica: põe "A carregar", faz o pedido numa coroutine e guarda Ok ou Erro
    private fun <T> carregar(destino: MutableStateFlow<Estado<T>>, pedido: suspend () -> T) {
        destino.value = Estado.Carregando
        viewModelScope.launch {                                   // coroutine ligada à vida do ViewModel
            val resultado: Estado<T> = try {
                Estado.Ok(pedido())
            } catch (e: CancellationException) {
                throw e                                           // cancelamento não é erro: deixa passar
            } catch (e: Exception) {
                Estado.Erro(mensagemDeErro(e))                    // correu mal: guarda a mensagem
            }
            destino.value = resultado                             // a UI atualiza sozinha
        }
    }

    private fun mensagemDeErro(e: Exception): String = when (e) {
        is HttpException -> if (e.code() == 401) "API key inválida (401). Confirma a Constants.API_KEY." else "Erro do servidor (${e.code()})."
        is IOException -> "Sem ligação à internet."
        else -> "Ocorreu um erro: ${e.message}"
    }
}
