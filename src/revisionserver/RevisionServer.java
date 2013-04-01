/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

import java.net.*;

/**
 *
 * @author Jaron
 */
public class RevisionServer {

    /**
     * @param args the command line arguments
     */
    
    public static byte[] respond(String in) {
        String[] lines=  in.split("\n");
        if(lines.length > 0) {
            String[] firstlinestuff = lines[0].split(" ");
            if(firstlinestuff.length > 0) {
                if(firstlinestuff[0].equals("GET")) {
                    //handle get request:
                    GetResponse gr = GetResponse.newFromClientRequest(lines);
                    return gr.getResponse();
                    
                }
            }
        }
        return "".getBytes();
            
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ServerSocket server = new ServerSocket(80);
            System.out.println("WAITING!");
            Socket skt = server.accept();
            System.out.println("ACCEPTED CONNECTION!");
            Thread connectionhandler = new Thread(new ConnectionHandler(server));
            
            connectionhandler.start();
            
            while(true) { }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
