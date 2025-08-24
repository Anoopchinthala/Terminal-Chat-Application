
package com.chatapp;

import jakarta.websocket.*;
import java.net.URI;
import java.util.Scanner;

@ClientEndpoint
public class Client {
    private static Session userSession = null;
    private static String username;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("✅ Connected to chat server");
        userSession = session;

        // Send username ONCE at connect
        if (username != null) {
            userSession.getAsyncRemote().sendText("##setname##" + username);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        if (message.startsWith("##setname##")) {
            // Handle system "join" notification cleanly
            String name = message.substring("##setname##".length()).trim();
            System.out.println("👋 " + name);
            return;
        }
        // Normal chat message
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("❌ Disconnected from server");
        userSession = null;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Ask username once at start
            System.out.print("Enter your name: ");
            username = scanner.nextLine();

            // Connect to server
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(Client.class, new URI("ws://localhost:8025/ws/chat"));

            System.out.println("👉 Type your messages (type 'exit' to quit):");

            // Chat input loop
            while (true) {
                String msg = scanner.nextLine();

                // Exit command
                if (msg.equalsIgnoreCase("exit")) {
                    if (userSession != null && userSession.isOpen()) {
                        userSession.close(); // Close socket properly
                    }
                    break;
                }

                // Send chat messages
                if (userSession != null && userSession.isOpen()) {
                    userSession.getAsyncRemote().sendText(msg);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
