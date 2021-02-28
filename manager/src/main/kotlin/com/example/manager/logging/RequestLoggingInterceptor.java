package com.example.manager.logging;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

import static net.logstash.logback.argument.StructuredArguments.value;

public class RequestLoggingInterceptor extends ServerHttpRequestDecorator {

    private static final Logger log = LoggerFactory.getLogger(ResponseLoggingInterceptor.class);


    private boolean logHeaders;

    public RequestLoggingInterceptor(ServerHttpRequest delegate, boolean logHeaders) {
        super(delegate);
        this.logHeaders = logHeaders;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return super.getBody().doOnNext(dataBuffer -> {
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String body = IOUtils.toString(baos.toByteArray(), "UTF-8");
                if (logHeaders)
                    log.info("Request: method={}, uri={}, headers={}, payload={}, audit={}", getDelegate().getMethod(),
                                getDelegate().getPath(), getDelegate().getHeaders(), body, value("audit", true));
                else
                    log.info("Request: method={}, uri={}, payload={}, audit={}", getDelegate().getMethod(),
                                getDelegate().getPath(), body, value("audit", true));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    baos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}