package com.sky.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by user1 on 2015-01-12.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Entity")
public class NotFoundException extends RuntimeException {
}
