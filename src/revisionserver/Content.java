/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

/**
 *
 * @author Jaron
 */
public class Content {
    String type = "text/html"; //default
    byte[] content = "".getBytes();
    String encoding = "UTF-8";
    public Content(byte[] content, String type, String encoding) {
        this.type = type;
        this.content = content;
        this.encoding = encoding;
    }    
   
    
    public Content(byte[] content) {
        this.content = content;
    }
    
    public Content(String content) {
        this.content = content.getBytes();
    }
    
    public String getType() {
        return type;
    }
    
    public byte[] getContent() {
        return content;
    }
    
    public String getEncoding() {
        return encoding;
    }
}
