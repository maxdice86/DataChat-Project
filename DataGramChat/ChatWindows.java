import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class ChatWindows extends JFrame implements ActionListener, KeyListener, MouseListener,Runnable {
    /**
     *
     */
    private static final long serialVersionUID = -8947457731868910432L;
    private JFrame window;
    private JPanel contents;
    private JPanel east;
    private JButton send;
    private JButton exit;
    private JButton clear;
    private JTextArea displayArea;
    private JTextField messageInput;
    public InetSocketAddress address;
    
    public ChatWindows(InetSocketAddress user){
        window = new JFrame();
        window.add(contents = new JPanel());
        contents.setLayout(new BorderLayout());
        contents.add(displayArea = new JTextArea(""),BorderLayout.CENTER);
        contents.add(east = new JPanel(), BorderLayout.EAST);
        contents.add(messageInput = new JTextField("**Type Message**"),BorderLayout.SOUTH);
        displayArea.setBackground(Color.WHITE);
        messageInput.setBackground(Color.YELLOW);
        east.setLayout(new GridLayout(3,1,5,5));
        east.add(clear = new JButton("Clear"), BorderLayout.EAST);
        east.add(exit = new JButton("Quit"));
        east.add(send = new JButton("Send"));
        send.addActionListener(this);
        exit.addActionListener(this);
        clear.addActionListener(this);
        messageInput.addKeyListener(this);
        messageInput.addMouseListener(this);
        JScrollPane bar = new JScrollPane(displayArea);
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contents.add(bar,BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setSize(480, 320);
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        address = user;
        window.setTitle("Chatting with: "+ address);
        
    }
    
    public void updateDisplay(String in){
        this.displayArea.append(in);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(send))
        {
            String input = messageInput.getText();
            this.messageInput.setText("");
            this.displayArea.append("You sent: "+ input + "\n");
            Messenger.msocket.send(input.trim(), address.getAddress(), address.getPort());
        }
        if (e.getSource().equals(clear))
        {
            this.displayArea.setText(null);
        }
        if(e.getSource().equals(exit))
        {
            window.dispose();
        }
        
    }
    
    @Override
    public void keyPressed(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_ENTER){
            String input = messageInput.getText();
            this.messageInput.setText("");
            this.displayArea.append("You sent: "+ input + "\n");
            Messenger.msocket.send(input.trim(), address.getAddress(), address.getPort());
            
            
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent m) {
        if(m.getSource().equals(messageInput)){
            if (messageInput.getText().equalsIgnoreCase("**Type Message**")){
                this.messageInput.setText("");
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}

