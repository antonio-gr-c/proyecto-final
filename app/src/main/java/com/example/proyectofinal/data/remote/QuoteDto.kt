// QuoteDto.kt
package com.example.proyectofinal.data.remote

import com.squareup.moshi.Json

data class QuoteDto(
    @Json(name = "q")        val text: String,
    @Json(name = "a")        val author: String?
)
