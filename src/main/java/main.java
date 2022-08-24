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
    private static ServicioURL surl = ServicioURL.getInstancia();
    private static ServicioVistaURL svurl = ServicioVistaURL.getInstancia();

    public static void main(String[] args) throws IOException {
        BootStrapServices.getInstancia().init();


        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });
            config.enableCorsForAllOrigins();
        }).start(7000);

        new ControladorUsuarios(app).aplicarRuta();
        new ControladorURL(app).aplicarRuta();

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
        app.get("/{id}",ctx -> {
            String id = ctx.pathParam("id");
            URL url = surl.findShortUrl(id);

            if(url != null){
                List<String> result = getInfoUser(ctx.userAgent());
                String ip = ctx.req.getRemoteAddr();
                svurl.crear(new VisitaURL(url,result.get(0),ip,result.get(1)));
                ctx.redirect(url.getLongUrl());
            }
            else {
                ctx.redirect("/inicio");
            }

        });

        System.out.println(new shortenerURL().encode(1000));
    }

    public static List<String> getInfoUser(String browserDetails){
        List<String> result = new ArrayList<String>();
        String userAgent = browserDetails;
        String user = userAgent.toLowerCase();

        String os = "";
        String browser = "";

        if (userAgent.toLowerCase().contains("windows"))
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().contains("mac"))
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            os = "Android";
        } else if(userAgent.toLowerCase().contains("iphone"))
        {
            os = "IOS";
        }else{
            os = "UnKnown";
        }

        if (user.contains("safari") && user.contains("version"))
        {
            browser = "Safari";
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            browser = "Opera";
        } else if (user.contains("chrome"))
        {
            browser = "Chrome";
        } else if (user.contains("firefox"))
        {
            browser = "Firefox";
        } else if(user.contains("rv") || user.contains("msie"))
        {
            browser = "Internet Explorer";
        } else
        {
            browser = "UnKnown";
        }

        result.add(browser);
        result.add(os);

        return result;
    }
}
