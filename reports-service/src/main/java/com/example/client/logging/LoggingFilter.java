package com.example.client.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;


@Slf4j
public class LoggingFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        var contentLength = serverWebExchange.getRequest().getHeaders().get("Content-Length");
        MDC.put("method", serverWebExchange.getRequest().getMethod().name());
        MDC.put("uri", serverWebExchange.getRequest().getURI().getPath());

        ServerWebExchangeDecorator exchangeDecorator = new ServerWebExchangeDecorator(serverWebExchange){
            @Override
            public ServerHttpRequest getRequest() {
                return new RequestLoggingInterceptor(super.getRequest());
            }

            @Override
            public ServerHttpResponse getResponse() {
                return new ResponseLoggingInterceptor(super.getResponse());
            }
        };
        return webFilterChain.filter(exchangeDecorator)
                .doOnEach(_ignore -> {
                    if(contentLength == null || contentLength.size() == 0) {
                        log.info("Request");
                    }
                })
                .doOnSuccess(unused -> {
                    if(contentLength == null || contentLength.size() == 0) {
                        log.info("Request");
                    }
                })
                .doOnError(throwable ->{
                    if(contentLength == null || contentLength.size() == 0) {
                        log.error(throwable.getMessage());
                    }
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
