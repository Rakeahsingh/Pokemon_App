package com.example.pokedex.pokemonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.modal.PokedexListEntry
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PokemonListScreen(
    navController: NavController
)
{
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.img), contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){

            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint : String = "",
    onSearch : (String) -> Unit = {}
){
    var text by remember{
       mutableStateOf("")
    }

    Box(modifier = modifier){
        BasicTextField(value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                }
        )
    }
}

@Composable
fun  PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
){
    val pokemonList by remember {viewModel.pokemonList}
    val endReached by remember {viewModel.endReached}
    val loadError by remember {viewModel.loadError}
    val isLoading by remember {viewModel.isLoading}

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if (pokemonList.size % 2 == 0){
            pokemonList.size / 2
        }else{
            pokemonList.size / 2 + 1
        }
        items(itemCount){
            if (it >= itemCount - 1 && !endReached){
                viewModel.loadPokemonPagination()
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
}

@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val defaulDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember{
        mutableStateOf(defaulDominantColor)
    }

    Box(contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaulDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "pokemon_Details_Screen/${dominantColor.toArgb()}/${entry.PokemonName}"
                )
            }
    ){
        Column {
            AsyncImage(
                model = entry.imageUrl,
                placeholder = rememberCoilPainter(request = ImageRequest.Builder(LocalContext.current)
                    .target{
                        viewModel.CalcDominantColor(it){ color->
                            dominantColor = color
                        }
                    }.build()),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = entry.PokemonName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}

@Composable
fun PokedexRow(
    rowIndex : Int,
    entries : List<PokedexListEntry>,
    navController: NavController
){
    Column{
        Row{
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier
                .width(16.dp))

            if (entries.size >= rowIndex * 2 +2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                )
            }else {
                Spacer(modifier = Modifier
                    .width(16.dp))
            }
        }
        Spacer(
            modifier = Modifier
            .height(16.dp))
    }
}




