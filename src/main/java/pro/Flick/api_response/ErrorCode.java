package pro.Flick.api_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TEST_ERROR(10000, HttpStatus.BAD_REQUEST, "테스트용으로 발생시킨 에러입니다."),
    NOT_FOUND_END_POINT(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API 엔드포인트입니다."),
    AD_INFO_NOT_FOUND(1, HttpStatus.NOT_FOUND, "해당 광고 정보를 찾을 수 없습니다."),
    INTERNAL_SEVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 알 수 없는 오류가 발생했습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
