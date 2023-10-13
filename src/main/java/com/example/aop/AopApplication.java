package com.example.aop;

import com.example.aop.test.SampleAdder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }

    //	@Bean
    public CommandLineRunner commandLineRunner() {
        SampleAdder adder = new SampleAdder();
        return args -> {
            adder.add(1, 2);
        };
    }
}
