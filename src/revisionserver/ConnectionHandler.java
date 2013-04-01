/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author Jaron
 */
public class ConnectionHandler implements Runnable {
    ServerSocket server;
    
    public ConnectionHandler(ServerSocket server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        while(!server.isClosed()) {
            try {
                Socket skt = server.accept();
                Connection connection = new Connection(skt);
                Thread connectionthread = new Thread(connection);
                connectionthread.start();
            } catch(IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            
        }
    }
    
}
