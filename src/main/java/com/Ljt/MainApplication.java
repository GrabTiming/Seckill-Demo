package com.Ljt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.Ljt.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class);

    }

}
