import Clases.BitlyURL;
import Clases.URL;
import Clases.VisitaURL;
import Clases.shortenerURL;
import Controladores.ControladorURL;
import Controladores.ControladorUsuarios;
import Servicios.BootStrapServices;
import Servicios.ServicioURL;
import Servicios.ServicioVistaURL;
import com.google.common.base.Optional;
import com.opsmatters.bitly.Bitly;
import com.opsmatters.bitly.api.model.v4.CreateBitlinkRequest;
import com.opsmatters.bitly.api.model.v4.CreateBitlinkResponse;
import com.opsmatters.bitly.api.model.v4.CreateFullBitlinkRequest;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        }).start(7000);

        app.get("/",ctx -> {
            ctx.redirect("/inicio");
        });
        app.get("/inicio",ctx -> {
            HashMap<String, Object> modelo = new HashMap<String,Object>();
            String url = ctx.sessionAttribute("url");

            if(!Objects.equals(url, "") && url != null){
              modelo.put("resultado",url);
              ctx.sessionAttribute("url","");
            }

            modelo.put("usuario",ctx.cookie("usuario"));
            modelo.put("role",ctx.cookie("role"));
            ctx.render("templates/inicio.html",modelo);
        });

        new ControladorUsuarios(app).aplicarRuta();
        new ControladorURL(app).aplicarRuta();
    }
}
