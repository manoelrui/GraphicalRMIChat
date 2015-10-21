package ufc.br.controller;

import ufc.br.model.RegistryMessage;
import ufc.br.model.ChatMessage;
import ufc.br.interfaces.ChatClient;
import ufc.br.interfaces.ChatServer;
import ufc.br.views.ServerView;
import java.net.InetAddress;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatServerImp extends UnicastRemoteObject implements ChatServer {
    public static final int SERVER_PORT = 1099;
    public static String SERVER_ADDRESS;
    public static final String CHAT_SERVER_NAME = "ChatServer";
    public static int messageCounter;
    Registry serverRegistry;
    Registry clientRegistry;
    public static ArrayList<RegistryMessage> clientRegistries;
    public static ArrayList<RegistryMessage> clientsToRemove;
    private static ChatServerImp instance;
    private static ServerView serverView;

    public static ChatServerImp getInstance(ServerView serverView) throws RemoteException{
        if(instance==null){
            instance = new ChatServerImp();
            instance.serverView = serverView;
            serverView.sendMessage("Server Started with success on " + SERVER_ADDRESS + ":" + SERVER_PORT + "\n");
        }
        return instance;
    }
  
    
    static {
        try {
            // Get address of host
            SERVER_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        messageCounter = 0;
    }

    private ChatServerImp() throws RemoteException{
        try {
            // Create the registry and bind the name and object
            serverRegistry = LocateRegistry.createRegistry(SERVER_PORT);
            serverRegistry.rebind(CHAT_SERVER_NAME, this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        clientRegistries = new ArrayList<RegistryMessage>();
        clientsToRemove = new ArrayList<RegistryMessage>();
    }

    @Override
    public void registryClient(RegistryMessage rMessage) {
        // TODO: Check client replications
        clientRegistries.add(rMessage);
        
        serverView.sendMessage("Number of clients: " + clientRegistries.size() + "\n");
        serverView.sendMessage("Nickname: " + rMessage.nickName + " - Host: " + rMessage.host + " - Port: " + rMessage.port + "\n");
        String newClientAlert = rMessage.nickName + " entered in the chat.";
        try {
            sendMessageToServer(new ChatMessage(messageCounter, CHAT_SERVER_NAME, newClientAlert));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToServer(ChatMessage chatMessage) throws RemoteException {

        // TODO: to create a callback to this behaviour.
        updateAliveClients();

        // TODO: to create a callback to this behaviour.
        alertClients();

        for(RegistryMessage clientMessage : clientRegistries) {
            try {
                ChatClient tempClient;
                clientRegistry = LocateRegistry.getRegistry(
                        clientMessage.host, (new Integer(clientMessage.port)).intValue());
                tempClient = (ChatClient) clientRegistry.lookup("[" + clientMessage.nickName + "]" + clientMessage.host + ":" + clientMessage.port);
                chatMessage.id = messageCounter;
                tempClient.receiveMessageFromServer(chatMessage);
            } catch (Exception e) {
                serverView.sendMessage("Exception: " + e.getMessage() + "\n");

            }
        }

        messageCounter++;
    }

    public void updateAliveClients() {
        for(RegistryMessage clientMessage : clientRegistries) {
            boolean clientIsAlive = false;
            try {
                ChatClient tempClient;
                clientRegistry = LocateRegistry.getRegistry(
                        clientMessage.host, (new Integer(clientMessage.port)).intValue());
                tempClient = (ChatClient) clientRegistry.lookup("[" + clientMessage.nickName + "]" + clientMessage.host + ":" + clientMessage.port);
                clientIsAlive = tempClient.isAlive();
            } catch (Exception e) {
                //e.printStackTrace();
            } finally {
                if(!clientIsAlive) {
                    clientsToRemove.add(clientMessage);
                }
            }
        }
    }

    public void alertClients() {
        if (!clientsToRemove.isEmpty()) {
            for (RegistryMessage clientMessage : clientsToRemove) {
                clientRegistries.remove(clientMessage);
            }

            for (RegistryMessage clientToAlertMessage : clientRegistries) {
                for (RegistryMessage clientToRemoved : clientsToRemove) {
                    try {
                        Registry clientRegistry;
                        ChatClient tempClient;
                        String leftMessage;
                        clientRegistry = LocateRegistry.getRegistry(
                                clientToAlertMessage.host, (new Integer(clientToAlertMessage.port)).intValue());
                        tempClient = (ChatClient) clientRegistry.lookup("[" + clientToAlertMessage.nickName + "]" + clientToAlertMessage.host + ":" + clientToAlertMessage.port);
                        leftMessage = clientToRemoved.nickName + " left the chat";
                        tempClient.receiveMessageFromServer(new ChatMessage(messageCounter, CHAT_SERVER_NAME, leftMessage));
                        messageCounter++;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            ChatServerImp.clientsToRemove.clear();
            serverView.sendMessage("Number of clients: " + clientRegistries.size() + "\n");
        }
    }
    
    
    public void alertClients(String text) {
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy  hh:mm:ss");
        serverView.sendMessage("[" +ft.format(now) +"] "+CHAT_SERVER_NAME +": " +text + "\n");
        for (RegistryMessage clientToAlertMessage : clientRegistries) {
                    try {
                        Registry clientRegistry;
                        ChatClient tempClient;
                        clientRegistry = LocateRegistry.getRegistry(clientToAlertMessage.host, (new Integer(clientToAlertMessage.port)).intValue());
                        tempClient = (ChatClient) clientRegistry.lookup("[" + clientToAlertMessage.nickName + "]" + clientToAlertMessage.host + ":" + clientToAlertMessage.port);
                        tempClient.receiveMessageFromServer(new ChatMessage(messageCounter, CHAT_SERVER_NAME, text));
                        messageCounter++;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
       }
    }
   

}
