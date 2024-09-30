/*
 * Copyright 2017-2024 Luis Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lerocha.opencambio.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var mdcEntries = getMdcEntries(request);
        try {
            Instant start = Instant.now();
            mdcEntries.forEach(MDC::put);
            chain.doFilter(request, response);
            Instant stop = Instant.now();
            log.info(getMessage(request, response, start, stop));
        } finally {
            mdcEntries.keySet().forEach(MDC::remove);
        }
    }

    private Map<String, String> getMdcEntries(ServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        if (request instanceof RequestFacade requestFacade) {
            map.put("method", requestFacade.getMethod());
            map.put("uri", requestFacade.getRequestURI());
            requestFacade.getParameterMap().forEach((key, value) -> map.put(key, Arrays.toString(value)));
            String forwardedFor = requestFacade.getHeader("X-FORWARDED-FOR");
            String ipAddress = forwardedFor != null ? forwardedFor.split(",")[0] : requestFacade.getRemoteAddr();
            map.put("ip", ipAddress);
            map.put("agent", requestFacade.getHeader("User-Agent"));
        }
        return map;
    }

    private String getMessage(ServletRequest request, ServletResponse response, Instant start, Instant stop) {
        StringBuilder builder = new StringBuilder();
        if (request instanceof RequestFacade requestFacade) {
            builder.append(requestFacade.getMethod()).append(" ")
                    .append(requestFacade.getRequestURI()).append(", ");
        }
        if (response instanceof ResponseFacade responseFacade) {
            builder.append("status=").append(responseFacade.getStatus()).append(", ")
                    .append("content-type=").append(responseFacade.getContentType()).append(", ");
        }
        builder.append("time=").append(Duration.between(start, stop).toMillis()).append(",");
        return builder.toString();
    }
}
