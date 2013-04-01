/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package revisionserver;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author Jaron
 */
public class GetResponse {

    private String filepath = "";
    private byte[] filecontents = new byte[1];
    private String hostname = "";
    private int contentlength = 0;
    private String status = "200 OK";
    private String contenttype = "text/html";
    private String contentencoding = "";
    private String location = "";

    /**
     * Gets the file from the relative path
     *
     * @param path
     * @return
     */
    public static Content loadFile(String path) throws Exception {
        byte[] contents = "".getBytes();
        String encoding = "UTF-8";
        Content content = new Content("Test!");
        String type = "";

        if (path.equals("/")) {
            path = "index.html";
        } else {
            path = path.substring(1); //remove the first slash
        }

        //remove all ..s

        path.replaceAll("\\.\\.", "");


        //get the file extension
        String[] ext = path.split("\\.");
        if (ext[0].equals("home")) {
            throw new Exception("301");
        }

        if (ext[1].equals("png") || ext[1].equals("jpg")) {
            File f = new File(path);
            InputStream mystream = new FileInputStream(f);
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) f.length()]; //make an appropriately sized buffer

            int size = mystream.read(buffer);
            //I guess this is irrelevant now
            byte[] reducedbuffer = new byte[size];
            for (int i = 0; i < size; i++) {
                reducedbuffer[i] = buffer[i];
            }
            contents = reducedbuffer;
            type = "image/" + ext[1];
            encoding = "";

        } else if (ext[1].equals("js")) {
            //oh god this is such a mess. whatever
            ServerScript script = new ServerScript(path);
            contents = script.invoke(null).getBytes();
        } else {
            FileReader file = null;
            try {
                file = new FileReader(path);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw new Exception("404");
            }
            BufferedReader br = new BufferedReader(file);
            String in;
            String concat = "";
            try {
                while ((in = br.readLine()) != null) {
                    concat = concat + in + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                return new Content("Unknown error, but something went wrong loading the file...");
            }

            contents = concat.getBytes();
        }
        return new Content(contents, type, encoding);

    }

    /**
     * Factory method to create a new GetRequest from the client request
     *
     * @param lines
     * @return
     */
    public static GetResponse newFromClientRequest(String[] lines) {
        //first line:
        String path = "";

        GetResponse get = new GetResponse();
        Content tcontent = new Content("");
        if (lines.length > 0) {
            String[] firstline = lines[0].split(" ");
            String filepath = firstline[1];
            byte[] filecontents = new byte[1024 * 8];

            try {
                tcontent = loadFile(filepath);
                filecontents = tcontent.getContent();
                get.setFilecontents(filecontents);
                get.setContenttype(tcontent.getType());

            } catch (Exception e) {
                if (e.getMessage().equals("404")) {
                    System.err.println("404 error!");
                    get.setStatus("404 Not Found");
                } else if (e.getMessage().equals("301")) {
                    System.err.println("301");
                    get.setStatus("301 Moved Permanently");
                    get.setLocation("index.html");
                } else {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }

            }
            get.setFilepath(filepath);
            get.setContentlength(filecontents.length);
            get.setEncoding(tcontent.getEncoding());

        } else {
            get.setFilecontents("Error 400, malformed request".getBytes());
        }

        return get;
    }

    /**
     * Form the response
     *
     * @return
     */
    public byte[] getResponse() {
        //create the header
        String header =
                "HTTP/1.1 " + status + "\n"
                + //"Date: " + (new SimpleDateFormat()).format(new Date()) + "\n" +
                "Connection: close\n"
                + "Server: Jaron's shitty revision server\n"
                + "Content-Length: " + (contentlength) + "\n"
                + ((!location.equals("")) ? "Location: " + location + "\n" : "")
                + "Content-Type: " + contenttype + "\n\n";


        byte[] out = new byte[header.length() + filecontents.length];
        //stick the header in the output bufefer
        for (int i = 0; i < header.length(); i++) {
            out[i] = header.getBytes()[i];
        }
        //and put the file contents in there while we're at it
        for (int i = 0; i < filecontents.length; i++) {
            out[i + header.length()] = filecontents[i];
        }

        return out;

    }

    /**
     * @return the filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @param filepath the filepath to set
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setContentlength(int x) {
        contentlength = x;
    }

    /**
     * @return the filecontents
     */
    public byte[] getFilecontents() {
        return filecontents;
    }

    /**
     * @param filecontents the filecontents to set
     */
    public void setFilecontents(byte[] filecontents) {
        this.filecontents = filecontents;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEncoding(String encoding) {
        this.contentencoding = encoding;
    }

    public void setContenttype(String type) {
        this.contenttype = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
