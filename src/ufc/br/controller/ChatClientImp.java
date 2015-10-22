package ufc.br.controller;

import ufc.br.model.RegistryMessage;
import ufc.br.model.ChatMessage;
import ufc.br.interfaces.ChatClient;
import ufc.br.interfaces.ChatServer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import ufc.br.views.ClientView;

/**
 * Created by rui on 10/12/15.
 */
public class ChatClientImp extends UnicastRemoteObject implements ChatClient {

    private String clientHost;
    private String clientNickName;
    private static ChatClientImp chatClientImp;
    private ChatServer chatServerImp;
    private ClientView clientView;
    
    public static ChatClientImp getInstance(String clientHost, String clientNickName, ClientView clientView) throws RemoteException{
        if(chatClientImp==null){
            chatClientImp = new ChatClientImp(clientHost,clientNickName, clientView);
        }
        return chatClientImp;
    }
        
    public ChatClientImp(String clientHost,String clientNickName, ClientView clientView) throws RemoteException {
        this.clientHost = clientHost;
        this.clientNickName = clientNickName;
        this.clientView =clientView;
    }

    public void receiveMessageFromServer(ChatMessage chatMessage) {
        System.out.println("Message " + chatMessage.id + " - " + chatMessage.nickname + ": " + chatMessage.text);
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy  hh:mm:ss");
        String text = "[" +ft.format(now) +"](" + chatMessage.id+") "+chatMessage.nickname +": " +chatMessage.text + "\n";
        if(clientView!=null){
            clientView.sendMessage(text);
        }
        
    }

    @Override
    public Boolean isAlive() throws RemoteException {
        return true;
    }

    public boolean start() {
        Registry serverRegistry;
        Registry clientRegistry;        
        String serverAddress = ChatServerImp.SERVER_ADDRESS;
        String serverPort = Integer.toString(ChatServerImp.SERVER_PORT);
        int portCont = 1090;
        String clientPort = String.valueOf(portCont);
        boolean tryToConnect = true;

        while(tryToConnect){
            try {
            // Create the registry and bind the name and object
            clientRegistry = LocateRegistry.createRegistry(portCont);
            clientRegistry.rebind("[" + clientNickName + "]" + clientHost + ":" + clientPort, getInstance(clientHost,clientNickName, clientView));
            tryToConnect = false;
            } catch (RemoteException e) {
                //e.printStackTrace();
                portCont++;
                clientPort = String.valueOf(portCont);
            }
        }
        

        try {
            serverRegistry = LocateRegistry.getRegistry(serverAddress, (new Integer(serverPort)).intValue());
            chatServerImp = (ChatServer) serverRegistry.lookup(ChatServerImp.CHAT_SERVER_NAME);
            chatServerImp.registryClient(new RegistryMessage(clientHost, clientPort, clientNickName, this));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void sendMessage(String textMessage){
       try {
           chatServerImp.sendMessageToServer(new ChatMessage(0, clientNickName, textMessage));
       } catch (ExportException e) {
           e.printStackTrace();
       } catch (RemoteException e) {
           e.printStackTrace();
       }
    }
}
