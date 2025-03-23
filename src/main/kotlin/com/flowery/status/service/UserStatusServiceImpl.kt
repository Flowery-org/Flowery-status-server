package com.flowery.status.service

import com.flowery.status.dto.UserStatusDto
import com.flowery.status.repository.UserStatusRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserStatusServiceImpl(private val userStatusRepository: UserStatusRepository) : UserStatusService {
    override fun getUserStatus(userId: String): UserStatusDto {
        val status = userStatusRepository.getUserStatus(userId) ?: throw IllegalArgumentException("User not found")
        val lastVisited = userStatusRepository.getUserLastVisited(userId) ?: LocalDateTime.now()

        return UserStatusDto(
            userId = userId,
            status = status,
            lastVisited = lastVisited
        )
    }

    override fun getBatchUserStatus(userIds: List<String>): Map<String, UserStatusDto> {
        val statusMap = userStatusRepository.getUsersStatus(userIds)

        return userIds.associateWith { userId ->
            getUserStatus(userId)
        }
    }

    override fun updateUserStatus(userId: String, timestamp: LocalDateTime): Boolean {
        return userStatusRepository.updateUserStatus(userId, timestamp)
    }
}