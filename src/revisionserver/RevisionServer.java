/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

import java.net.*;
import java.util.*;
import java.io.*;
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
                    GetResponse gr = GetResponse.newFromClientRequest(lines, new HashMap<String, String>());
                    return gr.getResponse();
                    
                }
                else if(firstlinestuff[0].equals("POST")) {
                    //get last line:
                    //split the last line by the ampersand character
                    String[] vars = lines[lines.length-1].split("\\&");
                    HashMap<String, String> postvars = new HashMap();
                    for(String s: vars) {
                        String[] pv = s.split("\\=");
                        //stick them values in the hashmap
                        if(pv.length == 2) {
                            try {
                                postvars.put(pv[0],URLDecoder.decode(pv[1], "UTF-8"));
                            } catch(UnsupportedEncodingException e) {
                                System.err.println("iunno lol");
                            }
                        }
                    }
                    System.out.println(in);
                    GetResponse gr = GetResponse.newFromClientRequest(lines, postvars);
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
