package app;

import java.net.http.HttpClient;

import app.context.BeanFactory;
import app.db.DataSource;
import app.handler.SearchHandler;
import app.service.StringUtils;
import io.jooby.Jooby;
import io.jooby.ServerOptions;

public class App extends Jooby {

    static BeanFactory context = new BeanFactory();

    {
        String portEnv = System.getenv("PORT");
        int port = context.getOrCreate(StringUtils.class).isEmpty(portEnv) ? 8080 : Integer.parseInt(portEnv);

        setServerOptions(new ServerOptions().setPort(port));
    }

    {
        get("/api/health", ctx -> "{\"status\":\"UP\"}");
        post("/api/search", ctx -> context.getOrCreate(SearchHandler.class).apply(ctx));
    }

    public static void main(String[] args) {
        context.add(HttpClient.class.getCanonicalName(), HttpClient.newHttpClient());
        DataSource dataSource = context.getOrCreate(DataSource.class);
        dataSource.migrate();

        runApp(args, App::new);
    }
}
