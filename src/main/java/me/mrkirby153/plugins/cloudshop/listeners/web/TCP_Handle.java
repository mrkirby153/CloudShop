package me.mrkirby153.plugins.cloudshop.listeners.web;

import me.mrkirby153.plugins.cloudshop.shop.EconHelper;

import java.io.BufferedWriter;
import java.io.IOException;

public class TCP_Handle {

    public static void handle(BufferedWriter writer, String line) throws IOException {
        String[] split = line.split(":");
        if(split.length <= 0)
            return;
        String commamnd = split[0];
        if(commamnd.equalsIgnoreCase("currBal")){
            if(split.length == 2){
                writer.write(EconHelper.getBalance(split[1]) + "\n");
                writer.flush();
            }
        }
    }
}
