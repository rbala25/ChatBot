package com.rishibala;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;

    public ClientHandler(Socket socket, String name) {
        this.clientSocket = socket;
        this.name = name;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(name + " says: " + inputLine);

                // Broadcast the received message to all clients (except the sender)
                for (ClientHandler client : ChatServer.getClients()) {
                    if (client != this) { // Exclude the sender client
                        client.sendToClient(name + " says: " + inputLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close resources when the client disconnects
                in.close();
                out.close();
                clientSocket.close();
                ChatServer.getClients().remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToClient(String message) {
        out.println(message);
    }
}
