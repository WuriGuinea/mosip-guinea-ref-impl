package io.mosip.autodeployer.deployerapp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DeployerAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DeployerAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {

            File dir = new File("/root/.ssh");
            boolean fileCreated = dir.mkdirs();

            if (fileCreated) {

                File file = new File(dir,"wuri-sandbox-aws.pem");
                if (file.createNewFile()) {
                    System.out.println("Dir/File created: "+ dir.getAbsolutePath() + "/" + file.getName() );

                    try {
                        FileWriter myWriter = new FileWriter(dir.getAbsolutePath() + "/" + file.getName());
                        System.out.println(System.getenv("AWSKEY"));
                        myWriter.write(System.getenv("AWSKEY"));
                        myWriter.close();

                        ProcessBuilder processBuilder = new ProcessBuilder();

                        // Run a shell command
                        processBuilder.command("bash", "-c", "chmod -R 600 /root/.ssh/");


                        try {

                            Process process = processBuilder.start();

                            StringBuilder output = new StringBuilder();

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream()));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                output.append(line + "\n");
                            }

                            int exitVal = process.waitFor();
                            if (exitVal == 0) {
                                System.out.println("Success!");
                                System.out.println(output);
                                //System.exit(0);
                            } else {
                                //abnormal...
                                System.out.println("Error");
                            }

                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File already exists.");
                }
            }
            else {
                System.out.println("Unable to created");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
