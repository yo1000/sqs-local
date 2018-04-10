package com.yo1000.sqs.local

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient
import org.elasticmq.NodeAddress
import org.elasticmq.rest.sqs.SQSRestServerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.aws.core.env.ResourceIdResolver
import org.springframework.cloud.aws.messaging.core.QueueMessageChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 *
 * @author yo1000
 */
@Configuration
class TestSqsConfiguration {
    companion object {
        const val BIND = "0.0.0.0"
        const val PROTOCOL = "http"
        const val HOST = "localhost"
        const val PORT = 9324
        const val CONTEXT_PATH = ""

        const val MANUALLY_QUEUE_NAME = "mQueue"
        const val MANUALLY_QUEUE_ENDPOINT = "$PROTOCOL://$HOST:$PORT"
        const val MANUALLY_QUEUE_URL = "$MANUALLY_QUEUE_ENDPOINT/queue/$MANUALLY_QUEUE_NAME"

        const val LISTENING_QUEUE_NAME = "lQueue"
        const val LISTENING_QUEUE_ENDPOINT = "$PROTOCOL://$HOST:$PORT"
        const val LISTENING_QUEUE_URL = "$LISTENING_QUEUE_ENDPOINT/queue/$LISTENING_QUEUE_NAME"

        val SQS_REST_SERVER = SQSRestServerBuilder
                .withInterface(BIND)
                .withPort(PORT)
                .withServerAddress(
                        NodeAddress(PROTOCOL, HOST, PORT, CONTEXT_PATH))
                .start().waitUntilStarted()
    }

    @Primary
    @Bean
    fun amazonSQS(): AmazonSQSAsync {
        val amazonSQS = AmazonSQSAsyncClient(BasicAWSCredentials("x", "x"))
        amazonSQS.setEndpoint(MANUALLY_QUEUE_ENDPOINT)
        amazonSQS.createQueue(MANUALLY_QUEUE_NAME)

        return amazonSQS
    }

    @Primary
    @Bean
    fun manuallyQueueMessagingTemplate(resourceIdResolver: ResourceIdResolver): SqsConfiguration.ManuallyQueueMessagingTemplate {
        val amazonSQS = AmazonSQSAsyncClient(BasicAWSCredentials("x", "x"))
        amazonSQS.setEndpoint(MANUALLY_QUEUE_ENDPOINT)
        amazonSQS.createQueue(MANUALLY_QUEUE_NAME)

        val template = SqsConfiguration.ManuallyQueueMessagingTemplate(amazonSQS, resourceIdResolver)
        template.defaultDestination = QueueMessageChannel(amazonSQS, MANUALLY_QUEUE_URL)

        return template
    }

    @Bean
    fun listeningQueueMessagingTemplate(resourceIdResolver: ResourceIdResolver): SqsConfiguration.ListeningQueueMessagingTemplate {
        val amazonSQS = AmazonSQSAsyncClient(BasicAWSCredentials("x", "x"))
        amazonSQS.setEndpoint(LISTENING_QUEUE_ENDPOINT)
        amazonSQS.createQueue(LISTENING_QUEUE_NAME)

        val template = SqsConfiguration.ListeningQueueMessagingTemplate(amazonSQS, resourceIdResolver)
        template.defaultDestination = QueueMessageChannel(amazonSQS, LISTENING_QUEUE_URL)

        return template
    }
}