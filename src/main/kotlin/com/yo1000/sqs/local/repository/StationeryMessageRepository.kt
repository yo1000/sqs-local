package com.yo1000.sqs.local.repository

import com.yo1000.sqs.local.SqsConfiguration
import org.springframework.stereotype.Repository

/**
 *
 * @author yo1000
 */
@Repository
class StationeryMessageRepository(
        val manuallyQueueMessagingTemplate: SqsConfiguration.ManuallyQueueMessagingTemplate
) {
    fun send(message: Stationery) {
        manuallyQueueMessagingTemplate.convertAndSend(message)
    }

    fun receive(): Stationery {
        return manuallyQueueMessagingTemplate.receiveAndConvert(Stationery::class.java)
    }
}
