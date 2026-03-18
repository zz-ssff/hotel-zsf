package com.hotel.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.hotel.pms.mapper")
public class HotelPmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelPmsApplication.class, args);
    }
}