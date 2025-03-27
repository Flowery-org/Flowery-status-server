package com.flowery.status.service

import com.flowery.status.dto.UserStatusDto
import com.flowery.status.repository.UserStatusRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserStatusServiceImpl(private val userStatusRepository: UserStatusRepository) : UserStatusService {
    override fun getUserStatus(userId: UUID): UserStatusDto {
        val status = userStatusRepository.getUserStatus(userId) ?: throw IllegalArgumentException("User not found")
        val lastVisited = userStatusRepository.getUserLastVisited(userId) ?: LocalDateTime.now()

        return UserStatusDto(
            userId = userId,
            status = status,
            lastVisited = lastVisited
        )
    }

    override fun getBatchUserStatus(userIds: List<UUID>): Map<UUID, UserStatusDto> {
        return userIds.associateWith { userId ->
            getUserStatus(userId)
        }
    }

    override fun updateUserStatus(userId: UUID, timestamp: LocalDateTime): Boolean {
        return userStatusRepository.updateUserStatus(userId, timestamp)
    }
}