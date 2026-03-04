package WebFramework;

public interface WebMethod {
    String execute(HttpRequest req, HttpResponse res);
}