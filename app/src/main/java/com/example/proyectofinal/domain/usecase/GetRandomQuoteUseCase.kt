package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.data.remote.QuoteDto
import com.example.proyectofinal.data.remote.QuoteRepository
import kotlinx.coroutines.flow.Flow

class GetRandomQuoteUseCase(
    private val repo: QuoteRepository = QuoteRepository()
) {
    operator fun invoke(): Flow<QuoteDto> = repo.getRandomQuote()
}
