package pro.Flick;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.Flick.trace.FieldLogTrace;
import pro.Flick.trace.LogTrace;
import pro.Flick.trace.ThreadLocalLogTrace;

@Configuration
public class FlickConfig {
    @Bean
    public LogTrace logTrace() {
        // return new FieldLogTrace();

        // 동시성 문제를 해결한 ThreadLocalLogTrace를 빈으로 등록
        return new ThreadLocalLogTrace();
    }
}
