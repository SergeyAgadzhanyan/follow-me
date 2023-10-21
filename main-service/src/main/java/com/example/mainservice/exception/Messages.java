package com.example.mainservice.exception;

import org.springframework.util.StringUtils;

public enum Messages {
    NOT_FOUND,
    VALIDATION_EXCEPTION,
    RESOURCE_NOT_FOUND,
    BAD_REQUEST,
    DB_CONFLICT,
    EVENT_NOT_PUBLISHED,
    DONT_HAVE_ENOUGH_RIGHTS,
    UPDATE_TIME_HAS_EXPIRED;

    public String getMessage() {
        return StringUtils.capitalize(this.name()
                .toLowerCase().replaceAll("_", " "));
    }
}
