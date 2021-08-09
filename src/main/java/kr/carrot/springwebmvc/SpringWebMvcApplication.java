package kr.carrot.springwebmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@EnableJms
@ServletComponentScan
@SpringBootApplication
public class SpringWebMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebMvcApplication.class, args);
    }

}
