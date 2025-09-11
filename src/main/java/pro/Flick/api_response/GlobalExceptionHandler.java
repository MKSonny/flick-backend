package pro.Flick.api_response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ApiResponse<?> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}", e.getMessage());
        return ApiResponse.fail(e);
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ApiResponse<?> handleBusinessException(BusinessException e) {
        return ApiResponse.fail(e);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }

//    @ExceptionHandler(value = {Exception.class})
//    public ApiResponse<?> handleException(Exception e) {
//        log.error("handleException() in GlobalExceptionHandler throw Exception : {}", e.getMessage());
//        e.printStackTrace();
//        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SEVER_ERROR));
//    }
}
