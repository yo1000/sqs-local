package com.yo1000.sqs.local.controller

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.aws.messaging.listener.Acknowledgment
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Controller

/**
 *
 * @author yo1000
 */
@Controller
@ConditionalOnMissingBean(StringMessageListener::class)
class StringMessageListener {
    @SqsListener(value = "lQueue", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    fun receive(message: String, ack: Acknowledgment) {
        println(message)
        ack.acknowledge()   // Only on `deletionPolicy = SqsMessageDeletionPolicy.NEVER`
    }
}
