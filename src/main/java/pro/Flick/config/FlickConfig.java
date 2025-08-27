package pro.Flick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.Flick.trace.LogTrace;
import pro.Flick.trace.ThreadLocalLogTrace;
import pro.Flick.trace.template.TraceTemplate;

@Configuration
public class FlickConfig {
    @Bean
    public LogTrace logTrace() {
        // return new FieldLogTrace();

        // 동시성 문제를 해결한 ThreadLocalLogTrace를 빈으로 등록
        return new ThreadLocalLogTrace();
    }

    @Bean
    public TraceTemplate traceTemplate() {
        return new TraceTemplate(logTrace());
    }
}
