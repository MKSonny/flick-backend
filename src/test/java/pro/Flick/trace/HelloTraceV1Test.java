package pro.Flick.trace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
class HelloTraceV1Test {
    @Test
    void begin_end() {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello"); // 로그 시작
        trace.end(status); // 로그 종료
    }

    @Test
    void begin_exception() {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello"); // 로그 시작
        trace.exception(status, new IllegalStateException()); // 예외 발생으로 로그 종료
    }
}