package com.flowery.status.service

import com.flowery.status.dto.UserStatusDto
import java.time.LocalDateTime
import java.util.UUID

interface UserStatusService {
    fun getUserStatus(userId: UUID): UserStatusDto
    fun getBatchUserStatus(userIds: List<UUID>): Map<UUID, UserStatusDto>
    fun updateUserStatus(userId: UUID, timestamp: LocalDateTime): Boolean
}