package com.example.a5minscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

// Cores da aplicação
val BackgroundColor = Color(0xFF14181C)
val SecondaryText = Color(0xFF99AABB)          // corrigido: 0xFF9AB tinha transparência total (alfa 0)

// Modelo que a interface usa para desenhar um filme (o "id" servirá para abrir o detalhe mais tarde)
data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: String
)

// Converte o filme da API (FilmeApi) no modelo da interface (Movie)
fun FilmeApi.paraMovie() = Movie(
    id = id,
    title = title,
    posterUrl = Constants.poster(posterPath) ?: "",                   // junta "https://image.tmdb.org/t/p/w342" ao "/abc.jpg"
    rating = voteAverage
        ?.takeIf { it > 0 }     // "sem avalições se for = 0.0
        ?.let { "%.1f".format(it) }
        ?: "Sem avaliações"
)

// HOME SCREEN
@Composable
fun HomeScreen(vm: HomeViewModel = viewModel()) {                      // viewModel() cria (ou reutiliza) o HomeViewModel
    val populares by vm.populares.collectAsState()                      // observa o estado: quando muda, o ecrã redesenha-se
    val melhores by vm.melhores.collectAsState()
    val emCartaz by vm.emCartaz.collectAsState()
    val brevemente by vm.brevemente.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 55.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "5minScreen",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Conteúdo da Home
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            // Título da página
            item {
                Text(
                    text = "Filmes",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 2.dp
                    )
                )
            }

            // Filmes populares
            item {
                MovieSection(
                    title = "Filmes Populares",
                    estado = populares,
                    aoTentarDeNovo = vm::recarregar
                )
            }

            // Melhores classificados
            item {
                MovieSection(
                    title = "Melhor Classificados",
                    estado = melhores,
                    aoTentarDeNovo = vm::recarregar
                )
            }

            // Em Cartaz
            item {
                MovieSection(
                    title = "Em Cartaz",
                    estado = emCartaz,
                    aoTentarDeNovo = vm::recarregar
                )
            }

            // Brevemente
            item {
                MovieSection(
                    title = "Brevemente",
                    estado = brevemente,
                    aoTentarDeNovo = vm::recarregar
                )
            }

        }
    }
}

// SECÇÃO DE FILMES: mostra spinner, erro ou a fila de cartazes conforme o estado
@Composable
fun MovieSection(
    title: String,
    estado: Estado<List<FilmeApi>>,
    aoTentarDeNovo: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Título da secção
        Text(
            text = title,
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = 20.dp,
                bottom = 12.dp
            )
        )

        when (estado) {
            is Estado.Carregando -> Box(                                // a carregar: spinner
                modifier = Modifier.fillMaxWidth().height(185.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00E054))
            }

            is Estado.Erro -> Column(                                   // erro: mensagem + botão
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(estado.mensagem, color = SecondaryText, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = aoTentarDeNovo) { Text("Tentar de novo") }
            }

            is Estado.Ok -> LazyRow(                                    // sucesso: lista horizontal de filmes
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(estado.dados) { filme -> MovieCard(filme.paraMovie()) }
            }
        }
    }
}

// CARTAZ DE FILME
@Composable
fun MovieCard(movie: Movie) {

    Column(
        modifier = Modifier.width(125.dp)
    ) {

        // Poster
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2C3440))                          // cor de fundo enquanto a imagem não chega
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nome do filme
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Classificação
        Text(
            text = "★ ${movie.rating}",
            color = Color(0xFFFFD166),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// RESTANTES ECRÃS (placeholders)
@Composable
fun SearchScreen() = Placeholder("Pesquisa")

@Composable
fun ActivityScreen() = Placeholder("Atividade")

@Composable
fun ProfileScreen() = Placeholder("Perfil")

@Composable
private fun Placeholder(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
