/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;
import java.util.*;
/**
 *
 * @author Jaron
 */
public class PostResponse extends GetResponse {
    
    
    public static GetResponse newFromClientRequest(String[] lines, HashMap<String, String> postvars) {
        GetResponse gresponse = GetResponse.newFromClientRequest(lines, postvars);
        return gresponse;
        
    }

}
