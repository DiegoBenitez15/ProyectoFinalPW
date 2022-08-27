import Controladores.ControladorURL;
import Controladores.ControladorUsuarios;
import Controladores.SoapControlador;
import Servicios.BootStrapServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.HashMap;
import java.util.Objects;

public class main {

    private static String modoConexion = "";

    public static void main(String[] args) {
        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });
            config.enableCorsForAllOrigins();
            config.server(() -> {
                return new SoapControlador().agregarWebServicesSoap();
            });
        }).start(7000);

        app.get("/",ctx -> {
            ctx.redirect("/inicio");
        });

        new ControladorUsuarios(app).aplicarRuta();
        new ControladorURL(app).aplicarRuta();
    }
}
