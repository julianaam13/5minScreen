package com.example.a5minscreen

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interface Retrofit: cada função = um pedido HTTP
interface TmdbService {
    @GET("movie/popular")                                        // GET https://api.themoviedb.org/3/movie/popular
    suspend fun populares(): FilmesResponse                      // suspend = corre numa coroutine, sem bloquear a app
    @GET("movie/top_rated")
    suspend fun melhores(): FilmesResponse

    @GET("movie/now_playing")
    suspend fun emCartaz(): FilmesResponse

    @GET("movie/upcoming")
    suspend fun brevemente(): FilmesResponse
}

// Cria o Retrofit UMA só vez e reaproveita-o
// (object = singleton; by lazy = só cria na 1.ª utilização)
object TmdbApi {
    val service: TmdbService by lazy {
        val cliente = OkHttpClient.Builder()
            .addInterceptor { chain ->                            // interceptor: mexe em TODOS os pedidos antes de saírem
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("api_key", Constants.API_KEY)   // acrescenta ?api_key=...
                    .addQueryParameter("language", Constants.IDIOMA)   // acrescenta &language=pt-PT
                    .addQueryParameter("region", "PT")                 // acrescenta region=PT
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())  // envia o pedido já com os parâmetros
            }
            .build()

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)                          // endereço base
            .client(cliente)                                      // usa o cliente com o interceptor
            .addConverterFactory(GsonConverterFactory.create())   // converte JSON -> classes Kotlin
            .build()
            .create(TmdbService::class.java)                      // Retrofit implementa a interface
    }
}
