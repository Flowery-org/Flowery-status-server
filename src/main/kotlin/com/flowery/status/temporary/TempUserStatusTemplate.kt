package com.flowery.status.temporary

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * RedisTemplate 대체하는 임시 class
 * - 인메모리 방식 사용 임시 사용 목적
 * - UserRepository에서 사용하는 기능만 간단하게 구현함
 * - 추후 Redis 연결 및 삭제 필요
 */
class TempUserStatusTemplate {
    private val userStatusMap = ConcurrentHashMap<String, String>()
    private val scheduler = Executors.newScheduledThreadPool(1)

    fun hasKey(key: String): Boolean {
        return userStatusMap.containsKey(key)
    }

    fun opsForValue() : OpsForValue {
        return OpsForValue()
    }

    fun expire(key: String, ttl: Long, timeUnit: TimeUnit): Boolean {
        if (!hasKey(key)) return false

        scheduler.schedule({
            userStatusMap.remove(key) //지정된 시간이 지났을 때의 동작
        }, ttl, timeUnit)
        return true
    }


    inner class OpsForValue {
        fun get(key: String) : String? {
            return userStatusMap[key]
        }
        fun set(key: String, value: String) {
            userStatusMap[key] = value
        }
    }

}