package com.example.proyectofinal.data.local

import com.example.proyectofinal.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id           = id,
    title        = title,
    description  = description,
    dueTimestamp = dueTimestamp,
    isDone       = isDone
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id           = id,
    title        = title,
    description  = description,
    dueTimestamp = dueTimestamp,
    isDone       = isDone
)
