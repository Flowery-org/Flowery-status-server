package com.flowery.status.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.flowery.status.dto.StatusUpdateRequestDto
import com.flowery.status.repository.UserStatusRepository
import com.flowery.status.temporary.TempUserStatusTemplate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.Mock
import org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime
import kotlin.test.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SpringBootTest
@AutoConfigureMockMvc
class UserStatusControllerTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userStatusRepository: UserStatusRepository

    @Mock
    private lateinit var mockClock : Clock

    @BeforeEach
    fun mockMvcSetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build()
    }

    @DisplayName("getUserStatus(): 이용자의 온라인 상태를 가져오는데 성공한다")
    @Test
    fun getUserStatus() {
        //given: 이용자 repository에 저장
        val url = "/status"
        val userId = "00"
        val lastVisited = LocalDateTime.now()
        userStatusRepository.updateUserStatus(userId, lastVisited)

        //when: 이용자 get 요청 전송
        val result = mockMvc.perform(get(url).param("userId", userId)
            .accept(MediaType.APPLICATION_JSON))

        //status code=OK, 이용자 상태="online", lastVisited=설정시간
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.status").value("online"))
            .andExpect(jsonPath("$.lastVisited").value(lastVisited.format(DateTimeFormatter.ISO_DATE_TIME)))
    }

    @DisplayName("getBatchUserStatus(): 이용자 리스트의 온라인 상태를 가져오는데 성공한다")
    @Test
    fun getBatchUserStatus() {
        //given: 이용자 한 명 repository에 저장
        val url = "/status/batch"
        val userId = "00"
        val lastVisited = LocalDateTime.now()
        userStatusRepository.updateUserStatus(userId, lastVisited)
        val users = listOf(userId)

        //when: 이용자 리스트로 get 요청
        val result = mockMvc.perform(get(url)
            .param("users", *users.toTypedArray())
            .accept(MediaType.APPLICATION_JSON))

        //then: 응답받은 이용자 상태 중 첫번째 이용자 상태 확인
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.00.userId").value(userId))
            .andExpect(jsonPath("$.00.status").value("online"))
            .andExpect(jsonPath("$.00.lastVisited").value(lastVisited.format(DateTimeFormatter.ISO_DATE_TIME)))
    }

    @DisplayName("updateUserStatus(): 이용자 상태 갱신에 성공한다")
    @Test
    fun updateUserStatus() {
        //given: 유저 상태 갱신
        val url = "/status/update"
        val userId = "00"
        val lastVisited = LocalDateTime.now()
        val requestBody = objectMapper.writeValueAsString(StatusUpdateRequestDto(userId, lastVisited))

        //when: API 호출
        val result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))

        //then: 사용자 상태 online으로 표시
        result.andExpect(status().isOk)
        Assertions.assertEquals("online", userStatusRepository.getUserStatus(userId))
    }
}