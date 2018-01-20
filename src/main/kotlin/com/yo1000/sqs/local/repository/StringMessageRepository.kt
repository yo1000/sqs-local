package com.yo1000.sqs.local.repository

import com.yo1000.sqs.local.SqsConfiguration
import org.springframework.stereotype.Repository

/**
 *
 * @author yo1000
 */
@Repository
class StringMessageRepository(
        val queueMessagingTemplate: SqsConfiguration.ManuallyQueueMessagingTemplate
) {
    fun send(message: String) {
        queueMessagingTemplate.convertAndSend(message)
    }

    fun receive(): String {
        return queueMessagingTemplate.receiveAndConvert(String::class.java)
    }
}