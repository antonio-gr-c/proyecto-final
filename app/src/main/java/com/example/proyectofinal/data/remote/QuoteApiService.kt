// QuoteApiService.kt
package com.example.proyectofinal.data.remote

import retrofit2.http.GET

interface QuoteApiService {

    @GET("random")
    suspend fun randomQuote(): List<QuoteDto>
}
