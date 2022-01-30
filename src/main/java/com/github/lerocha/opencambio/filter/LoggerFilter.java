package com.github.lerocha.opencambio.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestDetails = getRequestDetails(request);
        log.info("request; {}", requestDetails);

        chain.doFilter(request, response);

        log.info("response; {}; time={}", requestDetails, 0);
    }

    private String getRequestDetails(ServletRequest request) {
        if (request instanceof RequestFacade) {
            RequestFacade requestFacade = (RequestFacade) request;
            String params = requestFacade.getParameterMap().entrySet().stream()
                    .map(x -> String.format("%s=%s", x.getKey(), Arrays.toString(x.getValue())))
                    .collect(Collectors.joining("; "));
            return String.format("method=%s; uri=%s; %s", requestFacade.getMethod(), requestFacade.getRequestURI(), params);
        }
        return "";
    }
}
