package com.example.client.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class TraceIdFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> headers = exchange.getRequest().getHeaders().toSingleValueMap();
        return Mono.fromCallable(() -> {
            final long startTime = System.currentTimeMillis();

            return new ServerWebExchangeDecorator(exchange) {
                @Override
                public ServerHttpRequest getRequest() {
                    return new RequestLoggingInterceptor(super.getRequest(), false);
                }

                @Override
                public ServerHttpResponse getResponse() {
                    return new ResponseLoggingInterceptor(super.getResponse(), startTime, false);
                }
            };
        }).contextWrite(context -> {
            var traceId = "";
            if (headers.containsKey("X-B3-TRACEID")) {
                traceId = headers.get("X-B3-TRACEID");
                MDC.put("X-B3-TraceId", traceId);
            } else if (!exchange.getRequest().getURI().getPath().contains("/actuator")) {
                traceId = UUID.randomUUID().toString();
                MDC.put("X-B3-TraceId", traceId);
            }

            // simple hack to provide the context with the exchange, so the whole chain can get the same trace id
            Context contextTmp = context.put("X-B3-TraceId", traceId);
            exchange.getAttributes().put("X-B3-TraceId", traceId);

            return contextTmp;
        }).flatMap(chain::filter);
    }


}