package com.example.brewkery.data.remote

import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApiService {

    @GET("data.json")
    suspend fun getMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItemDetail(@Path("id") id: Int): MenuItem
}
