package com.flowery.status.controller

import com.flowery.status.dto.StatusUpdateRequestDto
import com.flowery.status.dto.UserStatusDto
import com.flowery.status.service.UserStatusService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/status")
class UserStatusController(private val userStatusService: UserStatusService) {

    @GetMapping
    fun getUserStatus(@RequestParam userId: String): ResponseEntity<UserStatusDto> {
        val userStatus = userStatusService.getUserStatus(userId)
        return ResponseEntity.ok(userStatus)
    }

    @GetMapping("/batch")
    fun getBatchUserStatus(@RequestParam users: List<String>): ResponseEntity<Map<String, UserStatusDto>> {
        val userStatuses = userStatusService.getBatchUserStatus(users)
        return ResponseEntity.ok(userStatuses)
    }

    @PostMapping("/update")
    fun updateUserStatus(@RequestBody request: StatusUpdateRequestDto): ResponseEntity<Boolean> {
        val result = userStatusService.updateUserStatus(request.userId, request.timestamp)
        return if (result) {
            ResponseEntity.ok(true)
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false)
        }
    }
}