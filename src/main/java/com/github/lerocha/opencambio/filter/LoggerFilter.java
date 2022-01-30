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

            return map.entrySet().stream()
                    .map(x -> String.format("%s=%s", x.getKey(), x.getValue()))
                    .collect(Collectors.joining("; "));
        }
        return "";
    }
}
