package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    /**
     * CGLIB 제약
     * 1. 부모클래스의 생성자를 체크해야 한다. -> CGLIB은 자식 클래스를 동적으로 생성하기 때문에 기본 생성자 필요
     * 2. 클래스에 'final'이 붙으면 상속이 불가능하다. -> CGLIB에서는 예외가 발생
     * 3. 메서드에 'final'이 붙으면 해당 메서드를 오버라이딩 할 수 없다. -> CGLIB에서는 프록시 로직이 동작하지 않는다.
     */

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        // cglib을 만드는 코드
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);  // 구체 클래스를 상속받아서 프록시를 생성
        enhancer.setCallback(new TimeMethodInterceptor(target));  // 프록시에 적용할 실행 로직을 할당
        // ConcreteService를 상속받아서 프록시를 만들기 때문에 캐스팅해도 된다.
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
}
