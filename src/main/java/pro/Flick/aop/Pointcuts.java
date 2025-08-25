package pro.Flick.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("within(*..*Service)")
    public void allService() {}

    @Pointcut("within(*..*Controller)")
    public void allController() {}

    @Pointcut("within(*..*Repository)")
    public void allRepository() {}

}
