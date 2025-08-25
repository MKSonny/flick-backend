package pro.Flick.decorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component {

    private Component component; // 내부에 실제 호출할 대상(RealComponent)을 가지고 있어야 함

    // 생성자를 통해 꾸며줄 실제 객체를 주입받는다.
    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        // 1. 실제 컴포넌트(RealComponent)의 operation()을 호출한다.
        String result = component.operation();

        // 2. 반환받은 결과(result)를 꾸며준다. (부가 기능)
        String decoratedResult = "***" + result + "***";

        log.info("꾸미기 적용 전='{}', 적용 후='{}'", result, decoratedResult);

        // 3. 꾸며진 결과를 반환한다.
        return decoratedResult;
    }
}
