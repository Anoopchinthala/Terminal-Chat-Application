
package com.chatapp;

import org.glassfish.tyrus.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/ws", null, Serverendpoint.class);

        try {
            server.start();
            System.out.println("✅ Server started at ws://localhost:8025/ws/chat");
            
            // Keep server alive until user presses Enter
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                scanner.nextLine();  // waits for user to press Enter
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
            System.out.println("❌ Server stopped.");
        }
    }
}
