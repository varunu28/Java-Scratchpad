package com.varun.reactive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlockUtil {

    public static void blockForTermination() throws IOException {
        System.out.println("Enter to terminate the program");
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
