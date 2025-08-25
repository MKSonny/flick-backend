package pro.Flick.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private Subject target; // 실제 호출할 대상 (RealSubject)
    private String cacheValue; // 데이터를 캐싱할 변수

    // 생성자를 통해 실제 객체의 참조를 주입받는다. (의존관계 주입)
    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");

        // 1. 캐시에 값이 없는 경우
        if (cacheValue == null) {
            // 2. 실제 객체(target)를 호출하여 결과를 가져온다.
            cacheValue = target.operation();
        }

        // 3. 캐시된 값을 반환한다.
        return cacheValue;
    }
}