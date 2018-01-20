package com.yo1000.sqs.local.repository

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 *
 * @author yo1000
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class StringMessageRepositoryTests {
    @Autowired
    lateinit var stringMessageRepository: StringMessageRepository

    @Test
    fun test_that_enqueued_value_is_equal_to_dequeued_value() {
        val message = "XyZ!"
        stringMessageRepository.send(message)
        Assert.assertEquals(message, stringMessageRepository.receive())
    }
}