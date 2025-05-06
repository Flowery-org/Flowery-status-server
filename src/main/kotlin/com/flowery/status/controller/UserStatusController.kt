package com.flowery.status.controller

import com.flowery.status.dto.StatusUpdateRequestDto
import com.flowery.status.dto.UserStatusDto
import com.flowery.status.service.UserStatusService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/status")
class UserStatusController(private val userStatusService: UserStatusService) {

    /**
     * 단일 사용자 조회 Endpoint
     *
     * @param userId
     * @return UserStatusDTO 이용자 온라인 상태 DTO
     */
    @GetMapping
    fun getUserStatus(@RequestParam userId: UUID): ResponseEntity<UserStatusDto> {
        try {
            val userStatus = userStatusService.getUserStatus(userId)
            return ResponseEntity.ok(userStatus)
        } catch (e : IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    /**
     * 다중 사용자 조회 Endpoint
     *
     * @param List<String> userId 배열
     * @return Map<String, UserStatusDTO> key는 userId이고, value는 이용자 온라인 상태 DTO
     */
    @GetMapping("/batch")
    fun getBatchUserStatus(@RequestParam users: List<UUID>): ResponseEntity<Map<UUID, UserStatusDto>> {
        val userStatuses = userStatusService.getBatchUserStatus(users)
        return ResponseEntity.ok(userStatuses)
    }

    /**
     * 상태 변경 및 TTL 갱신 Endpoint
     *
     * @param StatusUpdateRequestDTO 이용자 상태 갱신 요청 DTO
     * @return String 갱신 결과
     */
    @PostMapping("/update")
    fun updateUserStatus(@RequestBody request: StatusUpdateRequestDto): ResponseEntity<String> {
        return try{
            val result = userStatusService.updateUserStatus(request.userId, request.timestamp)
            if (result) {
                ResponseEntity.ok("User status updated")
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
            }
        } catch (e : Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User status update failed")
        }
    }
}