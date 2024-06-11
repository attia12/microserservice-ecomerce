package com.attia12.ecomerce.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
