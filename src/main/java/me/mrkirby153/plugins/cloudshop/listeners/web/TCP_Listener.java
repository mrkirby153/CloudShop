package me.mrkirby153.plugins.cloudshop.listeners.web;

import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_Listener extends Thread {

    private static ServerSocket server;
    private static int port = 555;


    public void run() {
        try {
            ChatHelper.sendToConsole("Started socket server!");
            ChatHelper.sendToConsole("Listening on port " + port);
            server = new ServerSocket(port);
            while (!Thread.currentThread().isInterrupted()) { // Repeat until killed.
                Socket socket = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        writer.write("Closing socket");
                        writer.flush();
                        break;
                    }
                    TCP_Handle.handle(writer, line);
                }
                reader.close();
                writer.close();
                socket.close();
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace(); //TODO: Better error handling
        }
    }

    public void kill(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
