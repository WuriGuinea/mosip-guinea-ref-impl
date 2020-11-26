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
public class DeployerAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DeployerAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Trying to reach ... AWS \n");
        try {
            JSch jsch = new JSch();
            String user = "centos";
            String host = "guinea-sandbox.mosip.net";
            int port = 22;
            String privateKey = "~/.ssh/wuri-sandbox-aws.pem";

            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                privateKey = "C:\\Users\\" + System.getProperty("user.name") + "\\.ssh\\wuri-sandbox-aws.pem";
            }

            jsch.addIdentity(privateKey);
            Session session = jsch.getSession(user, host, port);
            // System.out.println("session created.");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // System.out.println("session connected.....");
            Channel channel = session.openChannel("shell");
            OutputStream inputstream_of_channel = channel.getOutputStream();
            PrintStream commander = new PrintStream(inputstream_of_channel,
                    true);
            channel.setOutputStream(System.out, true);
            ((ChannelShell)channel).setPty(true);
            channel.connect();
            System.out.println("Connected to AWS VM");
            commander.print("sudo su mosipuser\n");
            commander.print("cd\n");
            commander.print("POD_UI=$(kc1 get pod | grep prereg-ui | awk '{print $1}') \n");
            commander.print("kc1 delete pod $POD_UI\n");
            commander.print("exit\n");
            commander.print("exit\n");
            commander.close();
            do {
                TimeUnit.SECONDS.sleep(1);
            } while (!channel.isEOF());
            session.disconnect();
            System.out.println("Process Finished ...");
        }
        catch (Exception e) {
            System.err.println(e);
        }

        System.exit(0);

    }
}
