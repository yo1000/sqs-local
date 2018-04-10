package com.yo1000.sqs.local.controller

import com.yo1000.sqs.local.SqsConfiguration
import com.yo1000.sqs.local.TestSqsConfiguration
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.aws.messaging.listener.Acknowledgment
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Controller
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 *
 * @author yo1000
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class StringMessageListenerTests {
    @Autowired
    lateinit var stringMessageListener: SpiedStringMessageListener
    @Autowired
    lateinit var listeningQueueMessagingTemplate: SqsConfiguration.ListeningQueueMessagingTemplate

    @Test
    fun test_that_queue_item_is_consumed_by_listeners_ack_invoke() {
        listeningQueueMessagingTemplate.convertAndSend("some message")

        stringMessageListener.countDownLatch.await()

        Assert.assertNull(listeningQueueMessagingTemplate.receive())
    }
}

@Primary
@Controller
class SpiedStringMessageListener : StringMessageListener() {
    val countDownLatch = CountDownLatch(1)

    @SqsListener(value = [(TestSqsConfiguration.LISTENING_QUEUE_URL)], deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    override fun receive(message: String, ack: Acknowledgment) {
        super.receive(message, ack)

        countDownLatch.countDown()
    }
}
