package com.example.battleshipmobile

import com.example.battleshipmobile.battleship.http.send
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule that launches a mock web server prior to the test execution and closes it
 * once the test finishes.
 */
class MockWebServerRule : TestRule {

    val server: MockWebServer
        get() = _server

    private lateinit var _server: MockWebServer

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                MockWebServer().use {
                    _server = it
                    it.start()
                    test.evaluate()
                }
            }
        }
}

class OkHttpExtensionsTests {

    @get:Rule
    val testRule = MockWebServerRule()

    @Test
    fun `send_returns_the_result_of_the_response_handler`() = runBlocking {

        // Arrange
        val expected = "Hello"
        val request = prepareRequest(expected)

        // Act
        val actual = request.send(OkHttpClient()) {
            this.body?.string()
        }

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handler_executes_on_another_thread`() = runBlocking {

        // Arrange
        val externalThread = Thread.currentThread().id
        val request = prepareRequest()

        // Act
        val handlerThreadId = request.send(OkHttpClient()) {
            Thread.currentThread().id
        }

        // Assert
        Assert.assertNotEquals(externalThread, handlerThreadId)
    }

    @Test(expected = Exception::class)
    fun `send_throws_the_exception_thrown_by_the_handler`(): Unit = runBlocking {

        // Arrange
        val request = prepareRequest()

        // Act
        request.send(OkHttpClient()) { throw Exception() }
    }

    private fun prepareRequest(expected: String = "some content"): Request {
        val mockServer = testRule.server
        mockServer.enqueue(MockResponse().setBody(expected))

        val url: HttpUrl = mockServer.url("/")
        return Request.Builder().url(url).build()
    }
}