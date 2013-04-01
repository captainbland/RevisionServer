/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;
import javax.script.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author Jaron
 */
public class ServerScript {
    String file = "";
    
    public ServerScript(String file) {
        this.file = file;
    }
    
    
    /**
     * Runs the script with its post arguments
     * @param postargs
     * @return Returns the output of the page
     * @throws ScriptException 
     */
    public String invoke(HashMap<String, String> postargs) throws ScriptException, FileNotFoundException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        StringWriter scriptwriter = new StringWriter();
        //give it post arguments
        engine.put("POST", postargs);
        
        //give it something to write to
        engine.put("page", scriptwriter);
        
        engine.eval(new java.io.FileReader(file));
        
        //get back that thing we wanted it to write to
        scriptwriter = (StringWriter)engine.get("page");
        
        //return whatever was written to it
        return scriptwriter.toString();
    }
}
