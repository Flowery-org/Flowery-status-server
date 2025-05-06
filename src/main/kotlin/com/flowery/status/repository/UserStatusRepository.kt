package com.flowery.status.repository

import java.time.LocalDateTime
import java.util.UUID

interface UserStatusRepository {
    fun getUserStatus(userId: UUID): String?
    fun getUserLastVisited(userId: UUID): LocalDateTime?
    fun updateUserStatus(userId: UUID, lastVisited: LocalDateTime): Boolean
    fun getUsersStatus(userIds: List<UUID>): Map<UUID, String>
}