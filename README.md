# Terminal-Chat-Application
A simple Java WebSocket-based chat application with support for multiple clients, real-time messaging, and database logging of chat history.

# Features
- Multi-client chat with WebSockets
- Real-time messaging in terminal
- SQLite database integration (Data.sql) to store chat history
- Simple Client-Server architecture
- Maven project for easy build & run

# Project Structure
src/main/java/com/chatapp/
│── Client.java           # Client-side WebSocket handler
│── Server.java           # Server setup (WebSocket)
│── Serverendpoint.java   # WebSocket endpoint (handles messages)
│── Database.java         # Handles SQLite operations
│── Main.java             # Entry point
│── Data.sql              # Database schema / setup
pom.xml                   # Maven dependencies
Commands.txt              # Helper commands
requirements.txt          # Requirements to this project

## Requirements
- Java 11 or higher
- Maven
- SQLite

