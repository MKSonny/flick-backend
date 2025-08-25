package pro.Flick.decorator;

import org.junit.jupiter.api.Test;

class DecoratorPatternTest {

    @Test
    void noDecorator() {
        // 1. 실제 객체 생성
        Component realComponent = new RealComponent();

        // 2. 클라이언트 생성 및 실제 객체 주입
        Client client = new Client(realComponent);

        // 3. 클라이언트 실행
        client.execute();
    }


    @Test
    void decoratorTest() {
        // 1. RealComponent를 직접 사용하는 경우
        Component realComponent = new RealComponent();
        Client client1 = new Client(realComponent);
        client1.execute();

        System.out.println("-------------------------");

        // 2. MessageDecorator를 사용하는 경우
        Component messageDecorator = new MessageDecorator(realComponent);
        Client client2 = new Client(messageDecorator);
        client2.execute();
    }
}