// QuoteApiService.kt
package com.example.proyectofinal.data.remote

import retrofit2.http.GET

interface QuoteApiService {
    // ejemplo con ZenQuotes: https://zenquotes.io/api/random
    @GET("random")
    suspend fun randomQuote(): List<QuoteDto>
}
