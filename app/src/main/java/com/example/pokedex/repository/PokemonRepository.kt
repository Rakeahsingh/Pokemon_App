package com.example.pokedex.repository

import com.example.pokedex.Api.PokeApi
import com.example.pokedex.data.PokemonList
import com.example.pokedex.data.pokemon
import com.example.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository
@Inject
constructor(
    private val api: PokeApi
)
{
    suspend fun getPokemonList(limit: Int, offset:Int) : Resource<PokemonList>{
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception){
            return Resource.Error("An Unknown Error Occured.")
        }
        return Resource.Sucess(response)
    }

    suspend fun getPokemonInfo(PokemonName: String) : Resource<pokemon>{
        val response = try {
            api.getPokemonInfo(PokemonName)
        } catch (e: Exception){
            return Resource.Error("An Unknown Error Occured.")
        }
        return Resource.Sucess(response)
    }
}