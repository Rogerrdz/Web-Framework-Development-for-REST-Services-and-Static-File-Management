package WebFramework;

import java.io.IOException;
import java.net.URISyntaxException;

import static WebFramework.WebFramework.get;
import WebFramework.examples.MathServices;


public class App {

    public static void main(String[] args) throws IOException, URISyntaxException {

        get("/pi", (req, res) -> String.valueOf(Math.PI));
        get("/euler", (req, res) -> String.valueOf(Math.E));
		HttpServer.staticfiles("webroot");
        MathServices.register(args);
        HttpServer.main(args);
    }
}