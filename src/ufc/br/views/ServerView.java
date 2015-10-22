/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufc.br.views;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import ufc.br.controller.ChatServerImp;
/**
 *
 * @author roney.reis
 */
public class ServerView extends javax.swing.JFrame {


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

    
    private JTextArea ChatBox=new JTextArea(10,45);
    private JScrollPane myChatHistory=new JScrollPane(ChatBox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JButton Start = new JButton("Start Server!");
    private JButton Close = new JButton("Exit");

    private InetAddress ServerAddress ;
    private static ChatServerImp chatServerImp;
    private static ServerView instance;
    public void sendMessage(String text){
            ChatBox.append(text);
     }
     
     private ServerView getInstance(){
        if(instance==null){
            instance = new ServerView();
        }
        return instance;
    }
     
    public ServerView(){
        setTitle("Server");
        setResizable(false);
        setSize(560,400);
        Container cp=getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(new JLabel("Server History"));
        cp.add(myChatHistory);
        //cp.add(new JLabel("Chat Box : "));
        //cp.add(myUserHistory);
        cp.add(Start);
        cp.add(Close);

         
         
        Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chatServerImp = ChatServerImp.getInstance(getInstance());
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
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
        instance = new ServerView();
    }
     
         

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
