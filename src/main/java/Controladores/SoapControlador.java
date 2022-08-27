package Controladores;

import com.sun.net.httpserver.HttpContext;
import jakarta.xml.ws.Endpoint;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import java.lang.reflect.Method;


public class SoapControlador {


    public SoapControlador() {

    }

    public Server agregarWebServicesSoap() {
        Server server = new Server();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        //Contexto donde estoy agrupando.
        try {
            HttpContext context = build(server, "/ws");

            //El o los servicios que estoy agrupando en ese contexto
            UrlWebServices wsa = new UrlWebServices();
            Endpoint endpoint = Endpoint.create(wsa);
            endpoint.publish(context);
            // Para acceder al wsdl en http://localhost:7000/ws/UrlWebServices?wsdl
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return server;
    }

    /**
     *
     * @param server
     * @param contextString
     * @return
     * @throws Exception
     */
    private HttpContext build(Server server, String contextString) throws Exception {
        JettyHttpServer jettyHttpServer = new JettyHttpServer(server, true);
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
        method.setAccessible(true);
        HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
        contextHandler.start();
        return ctx;
    }
}
