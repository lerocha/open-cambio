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

package com.github.lerocha.opencambio.configuration;

import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class OpenApiConfiguration {
    private static final Set<HttpStatus> defaultHttpStatuses = new HashSet<>(Arrays.asList(
            HttpStatus.BAD_REQUEST,
            HttpStatus.INTERNAL_SERVER_ERROR
    ));

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            // Default error responses.
            if (!operation.getParameters().isEmpty()) {
                defaultHttpStatuses.forEach(httpStatus -> {
                    if (!operation.getResponses().containsKey(httpStatus.value())) {
                        operation.getResponses().addApiResponse(
                                String.valueOf(httpStatus.value()),
                                new ApiResponse().description(httpStatus.getReasonPhrase())
                        );
                    }
                });
            }

            // Parameter sample values.
            operation.getParameters().forEach(p -> {
                switch (p.getName()) {
                    case "start":
                        p.example(LocalDate.now().withDayOfYear(1));
                        break;
                    case "date":
                    case "end":
                        p.example(LocalDate.now());
                        break;
                }
            });
            return operation;
        };
    }
}
