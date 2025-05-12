package com.flowery.status.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.TimeUnit

@Repository
class UserStatusRepositoryImpl(
    private val userStatusTemplate : StringRedisTemplate
) : UserStatusRepository {
    //val userStatusTemplate = TempUserStatusTemplate()

    companion object {
        private const val USER_KEY_PREFIX = "user:"
        private const val STATUS_ONLINE = "online"
        private const val STATUS_OFFLINE = "offline"
        private const val TTL_MINUTES = 5L
        private val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    }

    override fun getUserStatus(userId: UUID): String {
        val key = generateKey(userId)
        val hasKey = userStatusTemplate.hasKey(key)
        return if (hasKey) STATUS_ONLINE else STATUS_OFFLINE
    }

    override fun getUserLastVisited(userId: UUID): LocalDateTime? {
        val key = generateKey(userId)
        val lastVisitedStr = userStatusTemplate.opsForValue().get(key)
        return lastVisitedStr?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    override fun updateUserStatus(userId: UUID, lastVisited: LocalDateTime): Boolean {
        val key = generateKey(userId)
        userStatusTemplate.opsForValue().set(key, lastVisited.format(dateTimeFormatter), TTL_MINUTES, TimeUnit.MINUTES)
        return true
    }

    override fun getUsersStatus(userIds: List<UUID>): Map<UUID, String> {
        return userIds.associateWith { getUserStatus(it) }
    }

    private fun generateKey(userId: UUID): String {
        return USER_KEY_PREFIX + userId
    }
}