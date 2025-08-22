package pro.Flick.trace;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
    @Override
    public TraceStatus begin(String message) {
        syncTraceId(); // TraceId를 동기화(조회 또는 생성)하는 메서드
        TraceId traceId = traceIdHolder.get(); // ThreadLocal에서 현재 스레드의 TraceId를 조회
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
        releaseTraceId(); // TraceId를 해제(이전으로 되돌리거나 제거)하는 메서드
    }

    private void syncTraceId() {
        // 1. ThreadLocal에서 TraceId를 조회 (get)
        TraceId traceId = traceIdHolder.get();

        if (traceId == null) {
            // 2. 조회 결과가 null이면 (해당 스레드에서 첫 호출), 새로운 TraceId 생성 및 저장 (set)
            traceIdHolder.set(new TraceId());
        } else {
            // 3. 조회 결과가 있다면 (연속 호출), 레벨을 하나 증가시킨 새로운 TraceId 생성 및 저장 (set)
            traceIdHolder.set(traceId.createNextId());
        }
    }

    private void releaseTraceId() {
        // 1. ThreadLocal에서 현재 스레드의 TraceId를 조회 (get)
        TraceId traceId = traceIdHolder.get();

        if (traceId.isFirstLevel()) {
            // 2. 첫 번째 레벨이면 (해당 요청의 시작점), 추적이 끝났으므로 저장된 값을 완전히 제거 (remove)
            traceIdHolder.remove();
        } else {
            // 3. 첫 번째 레벨이 아니면, 이전 레벨로 되돌아감 (set)
            traceIdHolder.set(traceId.createPreviousId());
        }
    }
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
