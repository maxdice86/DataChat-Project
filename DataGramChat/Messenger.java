
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.sql.*;

public class Messenger extends JFrame implements ActionListener{
    
    /**
     *
     */
    private static final long serialVersionUID = 2622324629090569820L;
    private JPanel displayscreen;
    private JButton chat;
    private JButton login;
    private JButton quit;
    private JTextField destinationIp;
    private JTextField destinationPort;
    private JButton connect;
    private JFrame frame;
    private int port;
    private static  ArrayList<ChatWindows> windows = new ArrayList <ChatWindows>();
    private static  ArrayList<InetSocketAddress> allSockets = new ArrayList<InetSocketAddress>();
    public static Socket msocket;
    
    
    public Messenger(){
        //Messenger Main window setup
        
        add(displayscreen = new JPanel());
        displayscreen.setLayout(new FlowLayout());
        displayscreen.setBackground(Color.gray);
        displayscreen.add(this.login = new JButton("Login"));
        displayscreen.add(this.quit = new JButton("Quit"));
        displayscreen.add(this.chat = new JButton("Chat"));
        login.setBackground(Color.cyan);
        quit.setBackground(Color.cyan);
        chat.setBackground(Color.cyan);
        chat.addActionListener(this);
        login.addActionListener(this);
        quit.addActionListener(this);
        setSize(480,64);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Messenger Now Running");
    }
    
    public static void main(String[] args) {
      //  Messenger chatsession = new Messenger();
        
    }
    public static void getMessage(){
        DatagramPacket packet;
        do {
            packet = msocket.receive();
            if (packet != null) {
                InetSocketAddress from = (InetSocketAddress)packet.getSocketAddress();
                String message = new String(packet.getData());
                
                if (!allSockets.contains(from)){
                    ChatWindows chatSession = new ChatWindows(from);
                    windows.add(chatSession);
                    allSockets.add(from);
                    chatSession.updateDisplay("They: "+ message.trim()+"\n");
                    
                } else {
                    for (ChatWindows chat: windows){
                        if (chat.address.equals(from)) {
                            chat.updateDisplay("They: "+message.trim()+"\n");;
                        }
                    }
                }
            }
        } while(packet != null);
    }
    
    private void startChat(){
        frame = new JFrame();
        JPanel screen = new JPanel();
        JLabel dip = new JLabel("IP Address:");
        JLabel dpt = new JLabel("Port:");
        connect = new JButton("Connect");
        destinationIp =  new JTextField(12);
        destinationPort =  new JTextField(6);
        frame.add(screen);
        screen.setLayout(new FlowLayout());
        screen.setBackground(Color.gray);
        screen.add(dip);
        screen.add(destinationIp);
        screen.add(dpt);
        screen.add(destinationPort);
        screen.add(connect);
        connect.addActionListener(this);
        frame.setSize(420,96);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
        frame.setTitle("Enter Reciepient IP Address and Port #");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(login)){
            try {
                port = Integer.parseInt(JOptionPane.showInputDialog("Enter a port"));
                msocket = new Socket(port);
                setTitle("Logged in @: " + msocket.getIP() +" "+ port);
                //startThread();
                
            } catch (NumberFormatException n){
                
                JOptionPane.showMessageDialog(login, "Enter a valid port");
            }
        }
        
        if(e.getSource().equals(chat)){
            if (port != 0)
            {
                startChat();
            } else {
                JOptionPane.showMessageDialog(chat, "Login to start server");
            }
        }
        
        if(e.getSource().equals(quit))
        {
            System.exit(DISPOSE_ON_CLOSE);
        }
        if(e.getSource().equals(connect)){
            try{
                
                int outport = Integer.parseInt(destinationPort.getText());
                
                try {
                    InetAddress sendAddress = InetAddress.getByName(destinationIp.getText());
                    InetSocketAddress tosend = new  InetSocketAddress(sendAddress,outport);
                    if (!allSockets.contains(tosend))
                    {
                        ChatWindows chatSession = new ChatWindows(tosend);
                        windows.add(chatSession);
                        allSockets.add(tosend);
                    } else {
                        for (ChatWindows chat : windows)
                        {
                            if (chat.address.equals(tosend))
                            {
                                chat.updateDisplay("");
                            }
                        }
                    }
                    
                } catch (Exception t) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog(connect, "enter a valid address");
                    //System.exit(-1);
                }
                
            } catch (NumberFormatException n)
            {
                JOptionPane.showMessageDialog(connect, "enter a valid port");
            }
            destinationIp.setText("");
            destinationPort.setText("");
            frame.dispose();;
        }
    }
}
