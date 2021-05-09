package app;

import java.net.http.HttpClient;

import app.context.BeanFactory;
import io.jooby.Jooby;
import io.jooby.ServerOptions;

public class App extends Jooby {

    static BeanFactory context;

    {
        String portEnv = System.getenv("PORT");
        int port = portEnv == null || portEnv.isEmpty() ? 8080 : Integer.parseInt(portEnv);

        setServerOptions(new ServerOptions().setPort(port));
    }

    {
        get("/api/health", ctx -> "{\"status\":\"UP\"}");
    }

    public static void main(String[] args) {
        context = new BeanFactory();
        context.add(HttpClient.class.getCanonicalName(), HttpClient.newHttpClient());

        runApp(args, App::new);
    }
}
