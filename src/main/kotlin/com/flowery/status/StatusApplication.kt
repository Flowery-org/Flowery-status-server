package com.flowery.status

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StatusApplication

fun main(args: Array<String>) {
	runApplication<StatusApplication>(*args)
}
