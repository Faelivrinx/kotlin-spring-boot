package com.example.manager.logging

import com.example.manager.logger
import net.logstash.logback.argument.StructuredArguments.value
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels
import java.nio.charset.Charset

class RequestLoggingInterceptor(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {

    private val logger = logger<RequestLoggingInterceptor>()

    override fun getBody(): Flux<DataBuffer> {
        val byteArrayStream = ByteArrayOutputStream()
        return super.getBody().doOnNext {
            try {
                Channels.newChannel(byteArrayStream).write(it.asByteBuffer().asReadOnlyBuffer())
                val body = String(byteArrayStream.toByteArray(), Charset.defaultCharset())
                logger.info("Request: method {}, uri {}, body {}", value("method", delegate.method), value("uri", delegate.uri), value("body", body))
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    byteArrayStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }
}