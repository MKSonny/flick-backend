package pro.Flick.proxy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPatternTest {

    @Test
    void noProxyTest() {
        // 1. RealSubject 객체 생성
        RealSubject realSubject = new RealSubject();

        // 2. Client에 RealSubject 주입
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        // 3. Client의 execute() 메서드를 3번 호출
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest() {
        // 1. RealSubject 객체 생성
        Subject realSubject = new RealSubject();

        // 2. CacheProxy 객체 생성, 이 때 RealSubject를 주입
        Subject cacheProxy = new CacheProxy(realSubject);

        // 3. Client 객체 생성, 이 때 RealSubject가 아닌 CacheProxy를 주입
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);

        // 클라이언트 실행 (3번)
        client.execute(); // 첫 번째 호출
        client.execute(); // 두 번째 호출
        client.execute(); // 세 번째 호출
    }
}