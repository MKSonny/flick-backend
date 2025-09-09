package pro.Flick.api_response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiResponseTestController {

    @GetMapping("/success1")
    public ApiResponse<?> successWithData() {
        return ApiResponse.ok("this is ok response");
    }

    @GetMapping("/success2")
    public ApiResponse<?> successWithoutData() {
        return ApiResponse.ok(null);
    }

    @GetMapping("/exception1")
    public ApiResponse<?> testCustomException() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    @GetMapping("/exception2")
    public ApiResponse<?> testException() {
        String str = null;
        str.length();
        return ApiResponse.ok(null);
    }
}
