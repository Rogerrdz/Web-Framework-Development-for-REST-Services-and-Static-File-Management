package WebFramework.examples;



import java.io.IOException;
import java.net.URISyntaxException;

import WebFramework.HttpServer;
import static WebFramework.WebFramework.get;


public class MathServices {

    public static void register(String[] args) throws IOException, URISyntaxException {

        get("/App/pi", (req,res) -> "PI= " + Math.PI);
        get("/App/helloWorld", (req,res) -> "Hello World");
        get("/App/frommethod", (req,res) -> getEuler());
        get("/App/hello", (req, res) -> "Hello " + req.getValues("name"));
    }

    private static String getEuler() {
        return "Euler= " + Math.E;
    }
}