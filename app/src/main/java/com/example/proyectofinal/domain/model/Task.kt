package com.example.proyectofinal.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val dueTimestamp: Long,
    val isDone: Boolean = false
)
