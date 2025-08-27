//package pro.Flick.beans_config;
//
//import org.springframework.aop.Advisor;
//import org.springframework.aop.Pointcut;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.aop.support.NameMatchMethodPointcut;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import pro.Flick.trace.LogTrace;
//
//@Configuration
//public class BeanPostProcessorConfig {
//
//    @Bean
//    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace) {
//        String basePackage = "pro.Flick.member";
//        Advisor advisor = getAdvisor(logTrace);
//        return new PackageLogTracePostProcessor(basePackage, advisor);
//    }
//
//    private Advisor getAdvisor(LogTrace logTrace) {
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedNames("MC*");
//
////        Pointcut pointcut = Pointcut.TRUE;
//
//        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
//
//        return new DefaultPointcutAdvisor(pointcut, advice);
//    }
//}
