package WebFramework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
 
public class HttpServer {

    private static String staticFilesPath = "";
 
    public static void main(String[] args) throws IOException, URISyntaxException {

        Map<String, String> parameters = new HashMap<>();


        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
 
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
 
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
 
            boolean isFirstLine = true;
            String reqpath = "";
            String reqquery = "";
 
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
 
                if (isFirstLine) {
                    String[] flTokens = inputLine.split(" ");
                    String method = flTokens[0];
                    String struripath = flTokens[1];
                    String protocolversion = flTokens[2];
 
                    URI uripath = new URI(struripath);
                    reqpath = uripath.getPath();
 
                    System.out.println("Path: " + reqpath);
 
                    isFirstLine = false;

                    String query = uripath.getQuery();

                    if (query != null) {
                        String[] params = query.split("&");
                        for (String param : params) {
                            String[] keyValue = param.split("=");
                            if (keyValue.length == 2) {
                                parameters.put(keyValue[0], keyValue[1]);
                            }
                        }
                    }
                }
 
                if (!in.ready()) {
                    break;
                }
            }
 
            HttpRequest request = new HttpRequest(parameters);

            WebMethod currentwm = WebFramework.getRoute(reqpath);

            if (currentwm != null) {
                outputLine
                        = "HTTP/1.1 200 OK\n\r"
                        + "Content-Type: text/html\n\r"
                        + "\n\r"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Backend Service</title>\n"
                        + "</head>"
                        + "<body>"
                        + currentwm.execute(request, null)
                        + "</body>"
                        + "</html>";
 
            } else {
                String filePath = staticFilesPath + reqpath;

                InputStream fileStream = HttpServer.class.getResourceAsStream("/" + filePath);

                if (fileStream != null) {
                    BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
                    StringBuilder fileContent = new StringBuilder();
                    String line;

                    while ((line = fileReader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }

                    outputLine =
                        "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n\r\n" +
                        fileContent.toString();
                } else {
                    outputLine =
                        "HTTP/1.1 404 Not Found\r\n\r\n" +
                        "File not found";
                }
            }
 
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void get(String path, WebMethod wm) {
        WebFramework.get(path, wm);
    }

    
    public static void staticfiles(String path) {
        staticFilesPath = path;
    }
}