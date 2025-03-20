package com.flowery.status.service

import com.flowery.status.dto.UserStatusDto
import java.time.LocalDateTime

interface UserStatusService {
    fun getUserStatus(userId: String): UserStatusDto
    fun getBatchUserStatus(userIds: List<String>): Map<String, UserStatusDto>
    fun updateUserStatus(userId: String, timestamp: LocalDateTime): Boolean
}