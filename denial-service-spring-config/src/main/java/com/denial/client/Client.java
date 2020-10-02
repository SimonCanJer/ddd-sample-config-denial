package com.denial.client;

import ch.qos.logback.classic.Level;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    static final String SERVER_URL = "http://%s:%d/denial/process";
    static final String CLIENT_KEY = "clientId";
    static final AtomicBoolean smRun = new AtomicBoolean(true);
    static final ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @SuppressWarnings("all")
    static public void main(String[] args) {
        LOGGER.setLevel(Level.INFO);
        Client.class.getClassLoader().getResourceAsStream("application.properties");
        InputStreamReader isr = new InputStreamReader(System.in);
        URL url;
        System.out.println("Enter Server Port, kindly ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        try {
            while (isr.ready()) {
                smRun.set(true);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        Thread t = new Thread(() -> loop(port));
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }

    private static void loop(int port) {
        Random randomizer = new Random();
        InputStreamReader reader = new InputStreamReader(System.in);
        HttpPost post = new HttpPost(String.format(SERVER_URL,"localhost",port));
        while (true) {
            try {
                if (reader.ready())
                    break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            int id = randomizer.nextInt(10);
            long l= System.currentTimeMillis();
            int res=post(post,id);
            LOGGER.info("#client id="+id+" server response "+res+" ellapsed "+( System.currentTimeMillis()-l)+" Enter to stop");
            try {
                Thread.sleep(randomizer.nextInt(200));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

     private static int post(HttpPost post, int clientId) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(CLIENT_KEY, Integer.toString(clientId)));
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
            return HttpClients.createDefault().execute(post).getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
