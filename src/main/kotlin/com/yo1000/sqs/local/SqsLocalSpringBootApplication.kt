package com.yo1000.sqs.local

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SqsLocalSpringBootApplication

fun main(args: Array<String>) {
    runApplication<SqsLocalSpringBootApplication>(*args)
}
