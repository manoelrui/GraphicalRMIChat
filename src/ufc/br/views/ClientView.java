package ufc.br.views;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import ufc.br.controller.ChatClientImp;

public class ClientView extends javax.swing.JFrame{

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  /** Creates a new instance of myFrame */
    private JTextArea ChatBox=new JTextArea(10,45);
    private JScrollPane myChatHistory=new JScrollPane(ChatBox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JTextArea UserText = new JTextArea(5,40);
    private JScrollPane myUserHistory=new JScrollPane(UserText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JButton Send = new JButton("Send");
    private JButton Start = new JButton("Connect");
    private JButton Disconnect = new JButton("Disconnect");
    private JTextField Server=new JTextField(20);
    private JLabel myLabel=new JLabel("Server*:");
    private JLabel myNameLabel=new JLabel("User Name*:");
    private JTextField User=new JTextField(20);
    private ChatClientImp chatClientImp;
    private static ClientView instance;
     
    private ClientView getInstance(){
        if(instance==null){
            instance = new ClientView();
        }
        return instance;
    }
    
    public void sendMessage(String text){
        ChatBox.append(text);
        UserText.setText("");
    }
     
    public ClientView() {
        setResizable(false);
        setTitle("Client");
        setSize(560, 400);
        Container cp=getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(new JLabel("Chat History"));
        cp.add(myChatHistory);
        cp.add(new JLabel("Chat Box : "));
        cp.add(myUserHistory);
        cp.add(Send);
        cp.add(myLabel);
        cp.add(Server);
        cp.add(myNameLabel);
        cp.add(User);
        cp.add(Start);
        cp.add(Disconnect);
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chatClientImp != null) {
                    chatClientImp.sendMessage(UserText.getText());
                }
            }
        });
        Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(Server.getText()!=null && Server.getText().toString().length()>0&& User.getText().toString().length()>0 && User.getText()!=null){
                       chatClientImp = ChatClientImp.getInstance(Inet4Address.getLocalHost().getHostAddress().toString(), User.getText(), getInstance());
                        chatClientImp.clientNickName = User.getText();
                       if(!chatClientImp.start()){
                           ChatBox.append("It is not possible to connect to the server!\n");
                       }
                    }else{
                        JOptionPane.showMessageDialog(null, "Você precisa preencher todos os campos obrigatórios","Alerta", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);         
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        instance = new ClientView();
    }
         
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
