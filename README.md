# Terminal-Chat-Application
A simple Java WebSocket-based chat application with support for multiple clients, real-time messaging, and database logging of chat history.

# Features
- Multi-client chat with WebSockets
- Real-time messaging in terminal
- SQLite database integration (Data.sql) to store chat history
- Simple Client-Server architecture
- Maven project for easy build & run

# Project Structure
src/main/java/com/chatapp/ <br>
│── Client.java           # Client-side WebSocket handler <br>
│── Server.java           # Server setup (WebSocket) <br>
│── Serverendpoint.java   # WebSocket endpoint (handles messages) <br>
│── Database.java         # Handles SQLite operations <br>
│── Main.java             # Entry point <br>
│── Data.sql              # Database schema / setup <br>
pom.xml                   # Maven dependencies <br>
Commands.txt              # Helper commands <br>
requirements.txt          # Requirements to this project <br>

## Requirements
- Java 11 or higher
- Maven
- SQLite

## How to Run

1) Clone the repo:
    ```bash 
    git clone https://github.com/Anoopchinthala/Terminal-Chat-Application.git
    cd Terminal-Chat-Application

2) Build the project:
    ```bash
    mvn clean package

3) Start the Server:
    ```bash
    mvn exec:java@server

4) Start a Client:
    ```bash
    mvn exec:java@client

Type messages in terminal and chat in real-time 

## Database

All chat messages are saved in SQLite (Data.sql).
To view messages:
    ```bash
    SELECT * FROM messages;
