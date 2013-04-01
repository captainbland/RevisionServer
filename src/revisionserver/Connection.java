/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

import java.net.*;
import static revisionserver.RevisionServer.respond;

/**
 *
 * @author Jaron
 */
public class Connection implements Runnable {
    Socket skt;
    
    public Connection(Socket skt) {
        this.skt = skt;
    }
    
    public void run() {
        while(!skt.isClosed()) {
            try {
                 //handle requests...
                byte[] buffer = new byte[1024*8]; 
                int buffersize = skt.getInputStream().read(buffer);
                
                String response = new String(buffer, "UTF-8");
                if(buffersize > 0) {
                    response = response.substring(0, buffersize);
                }
                byte[] tosend = respond(response);
                //finally, send the response that we've created
                
                skt.getOutputStream().write(tosend);
                
                
                skt.close();
                return;
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
