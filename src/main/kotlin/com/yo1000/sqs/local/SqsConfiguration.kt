package com.yo1000.sqs.local

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.aws.core.env.ResourceIdResolver
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


/**
 *
 * @author yo1000
 */
@Configuration
class SqsConfiguration {
    @ConditionalOnMissingBean
    @Primary
    @Bean
    fun manuallyQueueMessagingTemplate(resourceIdResolver: ResourceIdResolver): ManuallyQueueMessagingTemplate {
        val amazonSQS = AmazonSQSAsyncClientBuilder.standard().build()
        return ManuallyQueueMessagingTemplate(amazonSQS, resourceIdResolver)
    }

    @ConditionalOnMissingBean
    @Bean
    fun listeningQueueMessagingTemplate(resourceIdResolver: ResourceIdResolver): ListeningQueueMessagingTemplate {
        val amazonSQS = AmazonSQSAsyncClientBuilder.standard().build()
        return ListeningQueueMessagingTemplate(amazonSQS, resourceIdResolver)
    }

    class ListeningQueueMessagingTemplate(amazonSqs: AmazonSQSAsync?, resourceIdResolver: ResourceIdResolver?)
        : QueueMessagingTemplate(amazonSqs, resourceIdResolver)

    class ManuallyQueueMessagingTemplate(amazonSqs: AmazonSQSAsync?, resourceIdResolver: ResourceIdResolver?)
        : QueueMessagingTemplate(amazonSqs, resourceIdResolver)
}
