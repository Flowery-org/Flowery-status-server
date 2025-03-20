package com.flowery.status.dto

import java.time.LocalDateTime

data class StatusUpdateRequestDto(
    val userId: String,
    val timestamp: LocalDateTime
)