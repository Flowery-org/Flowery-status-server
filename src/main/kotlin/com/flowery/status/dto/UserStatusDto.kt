package com.flowery.status.dto

import java.time.LocalDateTime

/**
 * 상태 업데이트 요청을 위한 DTO
 */
data class UserStatusDto(
    val userId: String,
    val status: String, // "online" 또는 "offline"
    val lastVisited: LocalDateTime
)
