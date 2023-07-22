package com.rishibala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(3000); // Choose any available port number
            System.out.println("Server started.");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for a client to connect
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

//                Scanner s = new Scanner(System.in);
//                System.out.print("Enter your name: ");
//                String name = s.nextLine();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Enter your name:");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String name = in.readLine();
                System.out.println(name + " has joined the chat.");
                out.println("-".repeat(40));
                out.println();

                ClientHandler clientHandler = new ClientHandler(clientSocket, name);
                clients.add(clientHandler); // Add the client handler to the list of clients
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }
}

