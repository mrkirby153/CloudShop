package me.mrkirby153.plugins.cloudshop.listeners.web;

import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPListener extends Thread {

    private static ServerSocket server;
    private static int port = 555;

    private Socket socket;

    public void run() {
        try {
            ChatHelper.sendToConsole("Started socket server!");
            ChatHelper.sendToConsole("Listening on port " + port);
            server = new ServerSocket(port);
            while (!Thread.currentThread().isInterrupted()) { // Repeat until killed.
                socket = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        writer.write("Closing socket");
                        writer.flush();
                        break;
                    }
                    TCPHandle.handle(writer, line);
                }
                reader.close();
                writer.close();
                socket.close();
            }
            server.close();
        } catch (SocketException e) {
            // Ignoree
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); //TODO: Better error handling
        }
    }

    public void kill() {
        try {
            server.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
