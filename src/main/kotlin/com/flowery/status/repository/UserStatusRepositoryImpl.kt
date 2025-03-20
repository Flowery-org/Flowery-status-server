package com.flowery.status.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Repository
class UserStatusRepositoryImpl(private val redisTemplate: RedisTemplate<String, Any>) : UserStatusRepository {

    companion object {
        private const val USER_KEY_PREFIX = "user:"
        private const val STATUS_ONLINE = "online"
        private const val STATUS_OFFLINE = "offline"
        private const val TTL_MINUTES = 5L
        private val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    }

    override fun getUserStatus(userId: String): String {
        val key = generateKey(userId)
        val hasKey = redisTemplate.hasKey(key)
        return if (hasKey == true) STATUS_ONLINE else STATUS_OFFLINE
    }

    override fun getUserLastVisited(userId: String): LocalDateTime? {
        val key = generateKey(userId)
        val lastVisitedStr = redisTemplate.opsForValue().get(key) as? String
        return lastVisitedStr?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    override fun updateUserStatus(userId: String, lastVisited: LocalDateTime): Boolean {
        val key = generateKey(userId)
        redisTemplate.opsForValue().set(key, lastVisited.format(dateTimeFormatter))
        return redisTemplate.expire(key, TTL_MINUTES, TimeUnit.MINUTES) ?: false
    }

    override fun getUsersStatus(userIds: List<String>): Map<String, String> {
        return userIds.associateWith { getUserStatus(it) }
    }

    private fun generateKey(userId: String): String {
        return USER_KEY_PREFIX + userId
    }
}