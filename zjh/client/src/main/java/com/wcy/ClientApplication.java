package com.wcy;

import com.wcy.netty.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        try {
            new NettyClient().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
