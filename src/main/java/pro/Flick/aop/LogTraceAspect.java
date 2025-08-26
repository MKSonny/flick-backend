//package pro.Flick.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import pro.Flick.trace.LogTrace;
//import pro.Flick.trace.TraceStatus;
//
//@Aspect // 이 클래스가 Aspect임을 선언합니다.
//public class LogTraceAspect {
//
//    private final LogTrace logTrace;
//
//    // 생성자를 통해 LogTrace 의존성을 주입받습니다.
//    public LogTraceAspect(LogTrace logTrace) {
//        this.logTrace = logTrace;
//    }
//
//    // @Around 애너테이션을 사용하여 어드바이스를 정의합니다.
//    // 그 값으로 포인트컷 표현식을 지정합니다.
//    @Around("execution(* pro.Flick.member..*(..))")
//    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
//        TraceStatus status = null;
//        try {
//            // 부가 기능: 로그 시작
//            String message = joinPoint.getSignature().toShortString();
//            status = logTrace.begin(message);
//
//            // 핵심 기능(타겟) 호출
//            Object result = joinPoint.proceed();
//
//            // 부가 기능: 로그 종료
//            logTrace.end(status);
//            return result;
//        } catch (Exception e) {
//            // 부가 기능: 예외 발생 시 로그
//            logTrace.exception(status, e);
//            throw e; // 예외를 다시 던져주어야 합니다.
//        }
//    }
//}