package com.flowery.status.dto

import java.time.LocalDateTime
import java.util.UUID

/**
 * 상태 업데이트 요청을 위한 DTO
 */
data class UserStatusDto(
    val userId: UUID,
    val status: String, // "online" 또는 "offline"
    val lastVisited: LocalDateTime
)
