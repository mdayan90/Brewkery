package com.example.brewkery.data.repository

import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MenuResponse
import com.example.brewkery.data.remote.BrewkeryApiService
import com.example.brewkery.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BrewkeryRepository(
    private val apiService: BrewkeryApiService = RetrofitClient.apiService
) {
    suspend fun getMenu(): Result<MenuResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMenu()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItemDetail(id: Int): Result<MenuItem> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItemDetail(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
