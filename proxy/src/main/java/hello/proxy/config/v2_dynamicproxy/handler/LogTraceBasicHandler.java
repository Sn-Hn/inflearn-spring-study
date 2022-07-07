package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


// JDK 동적 프록시는 인터페이스가 필수
// 인터페이스가 없다면 CGLIB이라는 바이트코드를 조작하는 특별한 라이브러리를 사용해야 한다.
@Slf4j
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;
        try {
            // 메서드 메타 정보에서 클래스 정보 -> 클래스 명
            String message = method.getDeclaringClass().getSimpleName() +
                    "." +
                    method.getName() +
                    "()";

            status = logTrace.begin(message);
            // 로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
