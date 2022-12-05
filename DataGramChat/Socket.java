import java.net.*;
import java.util.concurrent.*;

public class Socket {
    
    private InetAddress myAddress;
    private int port;
    private DatagramSocket socket = null;
    private ConcurrentLinkedQueue<DatagramPacket> messageQueue = new ConcurrentLinkedQueue<DatagramPacket>();
    
    
    public Socket(int port) {
        this.port = port;
        try {
            this.myAddress = InetAddress.getLocalHost();
            System.out.print(myAddress);
            this.socket = new DatagramSocket(port, this.myAddress);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        Thread receiveThread = new Thread(
                                          new Runnable() {
            public void run() {
                receiveThread();
            }
        });
        receiveThread.setName("\n Receive Thread For Port = " + this.port);
        receiveThread.start();
    }
    
    
    public void receiveThread() {
        
        do {
            byte[] inBuffer = new byte[2048];
            for ( int i = 0 ; i < inBuffer.length ; i++ ) {
                inBuffer[i] = ' ';
            }
            
            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
            
            try {
                // this is a blocking call
                socket.receive(inPacket);
            } catch(Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            String message = new String(inPacket.getData());
            
            System.out.println("\n"+message+ " was recieved on: " + this.port + "\n From Port: " + inPacket.getPort()
                               +" @IP: " + inPacket.getAddress());
            
            messageQueue.add(inPacket);
            Messenger.getMessage();
            
        } while(true);
    }
    
    public int getPort(){
        return port;
    }
    public InetAddress getIP(){
        return myAddress;
    }
    
    public DatagramPacket receive() {
        return messageQueue.poll();
    }
    
    
    public void send(String s, InetAddress destinationIP, int destinationPort) {
        
        byte[] outBuffer;
        
        try {
            outBuffer = s.getBytes();
            DatagramPacket outPacket = new DatagramPacket(outBuffer, s.length());
            outPacket.setAddress(destinationIP);
            outPacket.setPort(destinationPort);
            socket.send(outPacket);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

