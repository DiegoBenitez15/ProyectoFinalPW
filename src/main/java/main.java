import Controladores.ControladorUsuarios;
import Servicios.BootStrapServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.HashMap;

public class main {

    public static void main(String[] args){
        BootStrapServices.getInstancia().init();


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
            modelo.put("usuario",ctx.cookie("usuario"));
            modelo.put("role",ctx.cookie("role"));
            ctx.render("templates/inicio.html",modelo);
        });

        new ControladorUsuarios(app).aplicarRuta();
    }
}
