package pro.Flick.api_response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        boolean success,
        @Nullable
        T data,
        @Nullable
        ExceptionDto error
) {

    // 성공 응답(200 OK)을 생성하는 정적 팩토리 메서드
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.OK, true, data, null);
    }

    // 데이터 생성 성공 응답(201 created)을 생성하는 정적 팩토리 메서드
    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
    }

    public static <T> ApiResponse<T> fail(final BusinessException e) {
        return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }


    // 실패 응답을 생성하는 정적 팩토리 메서드
    public static <T> ApiResponse<T> fail(final CustomException e) {
        return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }
}
