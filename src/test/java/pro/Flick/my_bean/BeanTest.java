package pro.Flick.my_bean;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanTest {

    static class A {
        public void helloA() {
            System.out.println("A.helloA");
        }
    }

    static class B {
        public void helloB() {
            System.out.println("B.helloB");
        }
    }

    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("빈 후 처리기 실행 >> beanName={}, bean={}", beanName, bean.getClass());

            if (bean instanceof A) {
                log.info(">> A 객체를 B 객체로 바꿔치기 시작: {}", beanName);

                return new B();
            }

            return bean;
        }
    }

    @Test
    void basicConfig() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BasicConfig.class);

        A a = ac.getBean("beanA", A.class);
        a.helloA();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(B.class));
    }

    @Configuration
    static class BeanPostProcessorConfig {

        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor aToBPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Test
    @DisplayName("빈 후처리기를 사용하여 A 빈을 B 빈으로 교체한다.")
    void beanPostProcessor_swap() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        B b = ac.getBean("beanA", B.class);
        b.helloB();

        org.assertj.core.api.Assertions.assertThat(b).isInstanceOf(B.class);

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(A.class));
    }
}
