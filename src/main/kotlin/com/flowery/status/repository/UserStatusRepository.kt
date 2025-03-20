package com.flowery.status.repository

import java.time.LocalDateTime

interface UserStatusRepository {
    fun getUserStatus(userId: String): String?
    fun getUserLastVisited(userId: String): LocalDateTime?
    fun updateUserStatus(userId: String, lastVisited: LocalDateTime): Boolean
    fun getUsersStatus(userIds: List<String>): Map<String, String>
}