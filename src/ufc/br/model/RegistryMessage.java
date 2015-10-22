package ufc.br.model;

import ufc.br.interfaces.ChatClient;
import ufc.br.model.Message;

/**
 * Created by rui on 10/12/15.
 */
public class RegistryMessage extends Message {
    public String host;
    public String port;
    public String nickName;
    public ChatClient chatClient;

    public RegistryMessage(String host, String port, String nickName, ChatClient chatClient) {
        this.host = host;
        this.port = port;
        this.nickName = nickName;
        this.chatClient = chatClient;
    }
}
