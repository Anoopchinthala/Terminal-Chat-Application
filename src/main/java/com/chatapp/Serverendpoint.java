
package com.chatapp;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class Serverendpoint {

    // Track all connected clients
    private static final Set<Session> clients = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("✅ New connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            // Handle "set name" messages
            if (message.startsWith("##setname##")) {
                String name = message.substring("##setname##".length()).trim();
                session.getUserProperties().put("name", name);
                System.out.println("👋 " + name + " joined the chat.");
                broadcast("👋 " + name + " joined the chat.", session);
                return; // ✅ do NOT save this to DB
            }

            // Get sender name (default: "Anonymous")
            String sender = (String) session.getUserProperties().getOrDefault("name", "Anonymous");

            // Save message to DB
            Database db = new Database();
            db.saveMessage(sender, message);
            db.close();

            // Broadcast message
            broadcast(sender + ": " + message, session);

        } catch (Exception e) {
            System.err.println("⚠️ Error handling message: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        String name = (String) session.getUserProperties().getOrDefault("name", "A user");
        broadcast("❌ " + name + " left the chat.", session);
        System.out.println("❌ Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("❌ Error in session " + session.getId() + ": " + throwable.getMessage());
    }

    // Broadcast helper
    private void broadcast(String message, Session senderSession) {
        for (Session s : clients) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    System.err.println("⚠️ Broadcast error: " + e.getMessage());
                }
            }
        }
    }
}
