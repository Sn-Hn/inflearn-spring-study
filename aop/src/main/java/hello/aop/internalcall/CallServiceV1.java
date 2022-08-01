package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    // 생성자 주입 안된다. (순환 참조 - 자기 자신을 생성자로 주입할 수 없다.)
    // Spring Boot 2.6 버전부터는 순환 참조를 기본적으로 금지한다.
    // Requested bean is currently in creation: Is there an unresolvable circular reference?
    // 순환 참조를 해결하려면 application.properties에
    // spring.main.allow-circular-references=true를 추가해야 한다.
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
