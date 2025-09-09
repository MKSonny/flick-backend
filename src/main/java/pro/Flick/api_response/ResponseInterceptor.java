package pro.Flick.api_response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        /*
            모든 응답에 대해 이 advice를 적용하므로 true를 반환한다.
            특정 어노테이션이 붙은 경우나 특정 클래스 타입에만 적용하도록 조건을 걸 수도 있다.
         */
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ApiResponse<?>) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) body;

            HttpStatus status = apiResponse.httpStatus();

            response.setStatusCode(status);
        }

        return body;
    }
}
