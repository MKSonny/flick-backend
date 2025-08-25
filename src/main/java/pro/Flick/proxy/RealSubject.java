package pro.Flick.proxy;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RealSubject implements Subject{

    @Override
    public String operation() {
        sleep(1000);
        log.info("RealSubject 호출");
        return "데이터";
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
