package io.mosip.autodeployer.deployerapp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DeployerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeployerAppApplication.class, args);
    }
}
