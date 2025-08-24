package com.chatapp;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class Server {

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
    private static Database database = new Database();
    private static Map<Session, String> userNames = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("✅ New client joined: " + session.getId());

        // Send last 10 messages from DB to the new client
        for (String msg : database.getRecentMessages(10)) {
            session.getAsyncRemote().sendText("[History] " + msg);
        }

        session.getAsyncRemote().sendText("👉 Please enter your name:");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.startsWith("##setname##")) {
            String username = message.substring("##setname##".length()).trim();
            session.getUserProperties().put("username", username);
            userNames.put(session, username);

            // Only broadcast a nice message
            broadcast("👋 " + username + " joined the chat!");
            return;
        }

        String username = (String) session.getUserProperties().get("username");
        if (username != null){
            broadcast(username + ": " + message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        String username = userNames.remove(session);
        if (username != null) {
            broadcast("❌ " + username + " disconnected.");
            System.out.println("❌ Client disconnected: " + username);
        } else {
            System.out.println("❌ Client disconnected (no username set): " + session.getId());
        }
    }

    @SuppressWarnings("unused")
    private void sendMessage(Session session, String message) {
        // Using async to avoid blocking warnings
        session.getAsyncRemote().sendText(message);
    }

    private void broadcast(String message) {
        // Don't broadcast raw protocol commands
        if (message.startsWith("##setname##")) {
            return;
        }
        synchronized (clients) {
            for (Session client : clients) {
                client.getAsyncRemote().sendText(message);
            }
        }
    }
}

