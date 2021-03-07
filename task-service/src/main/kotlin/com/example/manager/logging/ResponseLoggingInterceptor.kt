package com.example.manager.logging

import com.example.manager.logger
import org.apache.commons.io.IOUtils
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels

class ResponseLoggingInterceptor(delegate: ServerHttpResponse) : ServerHttpResponseDecorator(delegate) {

    private val logger = logger<RequestLoggingInterceptor>()

    override fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void> {
        return super.writeAndFlushWith(body)
    }

    @Override
    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        var buffer = Flux.from(body)
        return super.writeWith(buffer.doOnNext { dataBuffer: DataBuffer ->
            val baos = ByteArrayOutputStream()
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer())
                val bodyRes: String = IOUtils.toString(baos.toByteArray(), "UTF-8")
                logger.info("Response: body $bodyRes")
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    baos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }
}