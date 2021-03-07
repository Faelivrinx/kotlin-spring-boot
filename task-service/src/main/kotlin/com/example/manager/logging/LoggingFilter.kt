package com.example.manager.logging

import com.example.manager.logger
import net.logstash.logback.argument.StructuredArguments.value
import org.slf4j.MDC
import org.springframework.cloud.sleuth.Span
import org.springframework.cloud.sleuth.Tracer
import org.springframework.core.Ordered
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.*
import reactor.core.publisher.Mono

class LoggingFilter : WebFilter, Ordered {

    private val logger = logger<LoggingFilter>()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val contentLength = exchange.request.headers["Content-Length"]

        MDC.put("method", exchange.request.method?.name)
        MDC.put("uri", exchange.request.uri.path)

        val exchangeDecorator = object : ServerWebExchangeDecorator(exchange) {
            override fun getRequest(): ServerHttpRequest {
                return RequestLoggingInterceptor(super.getRequest())
            }

            override fun getResponse(): ServerHttpResponse {
                return ResponseLoggingInterceptor(super.getResponse())
            }
        }
        return chain.filter(exchangeDecorator)
                .doOnEach {
                    if(contentLength == null || contentLength.size == 0) {
                        logger.info("Request: method {}, uri {}", value("method", exchange.request.method), value("uri", exchange.request.uri))
                    }
                }
                .doOnSuccess {
                    if(contentLength == null || contentLength.size == 0) {
                        logger.info("Response: method {}, uri {}",
                                    value("method", exchangeDecorator.delegate.request.method),
                                    value("uri", exchangeDecorator.delegate.request.uri))
                    }
                }
                .doOnError {
                    logger.error("Response: method {}, uri {}",
                                 value("method", exchangeDecorator.delegate.request.method),
                                 value("uri", exchangeDecorator.delegate.request.uri))
                }
    }

    override fun getOrder(): Int {
        return -999
    }
}