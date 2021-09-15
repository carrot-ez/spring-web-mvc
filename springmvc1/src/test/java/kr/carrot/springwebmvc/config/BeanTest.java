package kr.carrot.springwebmvc.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
public class BeanTest {

    @Autowired
    BeanConfigTest beanConfigTest;

    @Autowired
    TestBean testBean;


    @Test
    public void 빈생성테스트() {

        System.out.println(testBean);

        System.out.println(beanConfigTest.testBean());
        System.out.println(beanConfigTest.testBean());

        beanConfigTest.printTestBean();

    }
}


@Configuration
class BeanConfigTest {

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

    public void printTestBean() {
        System.out.println(testBean());
        System.out.println(testBean());
    }
}


class TestBean {

}
