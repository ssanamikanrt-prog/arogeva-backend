package com.hospital.Arogeva.advices;


import io.swagger.v3.oas.annotations.Hidden;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Hidden
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        // Exclude Swagger endpoints and specific message converters that cause ClassCastExceptions
        if (converterType.equals(StringHttpMessageConverter.class) ||
            converterType.equals(ByteArrayHttpMessageConverter.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();
        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui")) {
            return body;
        }

        if(body instanceof ApiResponse<?>) {
            return body;
        }

        return new ApiResponse<>(body);
    }
}