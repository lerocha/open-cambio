/*
 * Copyright 2017-2022 Luis Rocha
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

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Instant start = Instant.now();
        String requestDetails = getRequestDetails(request);
        log.info("request; {}", requestDetails);

        chain.doFilter(request, response);

        Instant stop = Instant.now();
        log.info("response; {}; time={}", requestDetails, Duration.between(start, stop).toMillis());
    }

    private String getRequestDetails(ServletRequest request) {
        if (request instanceof RequestFacade) {
            RequestFacade requestFacade = (RequestFacade) request;
            Map<String, String> map = new LinkedHashMap<>();
            map.put("method", requestFacade.getMethod());
            map.put("uri", requestFacade.getRequestURI());
            requestFacade.getParameterMap().forEach((key, value) -> map.put(key, Arrays.toString(value)));
            String forwardedFor = requestFacade.getHeader("X-FORWARDED-FOR");
            String ipAddress = forwardedFor != null ? forwardedFor.split(",")[0] : requestFacade.getRemoteAddr();
            map.put("ipAddress", ipAddress);

            return map.entrySet().stream()
                    .map(x -> String.format("%s=%s", x.getKey(), x.getValue()))
                    .collect(Collectors.joining("; "));
        }
        return "";
    }
}
