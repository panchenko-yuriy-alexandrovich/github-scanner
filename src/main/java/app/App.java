package app;

import io.jooby.Jooby;
import io.jooby.ServerOptions;

public class App extends Jooby {

    {
        String portEnv = System.getenv("PORT");
        int port = portEnv == null || portEnv.isEmpty() ? 8080 : Integer.parseInt(portEnv);

        setServerOptions(new ServerOptions().setPort(port));
    }

    {
        get("/health", ctx -> "{\"status\":\"UP\"}");
    }

    public static void main(String[] args) {
        runApp(args, App::new);
    }
}
