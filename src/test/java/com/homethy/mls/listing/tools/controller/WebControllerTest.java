package com.homethy.mls.listing.tools.controller;

import org.junit.Test;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * Created by jia on 17-7-14.
 */
public class WebControllerTest {

    @Test
    public void readENDTest() throws IOException {
//        new WebController().readFile();
        Date date = new Date();
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetAddress host = InetAddress.getLocalHost();
        System.out.println();
    }
}
