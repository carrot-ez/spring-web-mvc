package kr.carrot.springwebmvc.springmvc.controller;

import kr.carrot.springwebmvc.springmvc.dto.SimpleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spring-mvc")
@Slf4j
public class BasicController {

    @GetMapping("/params")
    public String paramsController(@ModelAttribute SimpleDto data) {

        log.info("data={}", data);
        return "ok";
    }


}
