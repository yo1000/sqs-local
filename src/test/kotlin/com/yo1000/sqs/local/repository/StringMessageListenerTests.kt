package com.yo1000.sqs.local.repository

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
import org.springframework.stereotype.Repository
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
    lateinit var stringMessageListener: StringMessageListener
    @Autowired
    lateinit var listeningQueueMessagingTemplate: SqsConfiguration.ListeningQueueMessagingTemplate

    @Test
    fun manualDeletion_withAcknowledgmentCalled_shouldSucceedAndDeleteMessage() {
        listeningQueueMessagingTemplate.convertAndSend("some message")
        Assert.assertTrue((stringMessageListener as SpiedStringMessageListener)
                .countDownLatch.await(15, TimeUnit.SECONDS))
    }
}

@Primary
@Repository
class SpiedStringMessageListener : StringMessageListener() {
    val countDownLatch = CountDownLatch(1)

    @SqsListener(value = TestSqsConfiguration.LISTENING_QUEUE_URL, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    override fun listen(message: String, ack: Acknowledgment) {
        super.listen(message, ack)
        countDownLatch.countDown()
    }
}
