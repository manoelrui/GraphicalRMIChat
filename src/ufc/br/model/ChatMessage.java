package ufc.br.model;

/**
 * Created by rui on 10/12/15.
 */
public class ChatMessage extends Message {
    public String nickname;
    public String text;

    public ChatMessage(int id, String nickname, String text) {
        this.id = id;
        this.nickname = nickname;
        this.text = text;
    }
}
