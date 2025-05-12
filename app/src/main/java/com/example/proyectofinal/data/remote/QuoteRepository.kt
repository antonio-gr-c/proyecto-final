package com.example.proyectofinal.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuoteRepository(
    private val api: QuoteApiService = NetworkModule.api
) {
    /**
     * Emite un QuoteDto al pedir una sola frase.
     * Asumimos que la lista siempre viene con 1 elemento.
     */
    fun getRandomQuote(): Flow<QuoteDto> = flow {
        val list = api.randomQuote()
        if (list.isNotEmpty()) emit(list.first())
    }
}
