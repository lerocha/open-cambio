/*
 * Copyright 2017-2023 Luis Rocha
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

package com.github.lerocha.opencambio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleBadRequests(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), null, null, request);
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, null, request);
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

}
