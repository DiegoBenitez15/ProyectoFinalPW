package Controladores;

import Clases.URL;
import Clases.Usuario;
import Clases.VisitaURL;
import Clases.shortenerURL;
import Servicios.ServicioURL;
import Servicios.ServicioUsuario;
import Servicios.ServicioVistaURL;
import io.javalin.Javalin;

import java.lang.module.FindException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControladorURL {
    private Javalin app;
    private ServicioURL surl = ServicioURL.getInstancia();
    private ServicioVistaURL svurl = ServicioVistaURL.getInstancia();
    private ServicioUsuario su = ServicioUsuario.getInstancia();

    public ControladorURL(Javalin app) {
        this.app = app;
    }

    public void aplicarRuta(){
        app.routes(() -> {
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
            path("/url", () -> {
                post("/acortar", ctx -> {
                    String url = ctx.formParam("url");
                    String id = ctx.cookie("usuario");
                    String usuario = null;

                    if(id != null){
                        usuario = su.find(id).getUsuario();
                    }else {
                        usuario = ctx.req.getSession().getId();
                    }

                    url = surl.createShortLink(url,usuario);


                    ctx.sessionAttribute("url",url);

                    ctx.redirect("/inicio");
                });
                get("/listado",ctx -> {
                    HashMap<String,Object> modelo = new HashMap<String,Object>();

                    if(ctx.cookie("usuario") != null) {
                        modelo.put("urls", surl.findByUser(ctx.cookie("usuario"), ctx.cookie("role")));
                    }else{
                        modelo.put("urls", surl.findByUser(ctx.req.getSession().getId(),"2"));
                    }
                    modelo.put("usuario",ctx.cookie("usuario"));
                    modelo.put("role",ctx.cookie("role"));
                    ctx.render("templates/listado_url.html",modelo);
                });
                get("/eliminar",ctx -> {
                    String id = ctx.queryParam("id");
                    URL url = surl.find(id);
                    url.setActive(Boolean.FALSE);
                    surl.editar(url);
                    ctx.redirect("/url/listado");
                });
                get("/estadisticas",ctx -> {
                    HashMap<String,Object> modelo = new HashMap<String,Object>();
                    String id = ctx.queryParam("id");
                    String fecha = ctx.queryParam("fecha");
                    URL url = surl.find(id);

                    if(fecha == null){
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        fecha = dateFormat.format(new Date());
                    }

                    modelo.put("id",url.getId());
                    modelo.put("fecha",fecha);
                    modelo.put("hoy",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    modelo.put("soClicks",svurl.getSO((int) url.getId()));
                    modelo.put("browserClicks",svurl.getBrowsers((int) url.getId()));
                    modelo.put("hoyClicks",svurl.getClicksHoy((int) url.getId()));
                    modelo.put("hoursClicks",svurl.getClicksHours((int) url.getId(),fecha));
                    modelo.put("totalClicks",svurl.getClicksTotal((int) url.getId()));
                    ctx.render("/templates/estadisticas_url.html",modelo);
                });
            });
        });
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
        } else if(userAgent.toLowerCase().contains("mac") || userAgent.toLowerCase().contains("iphone"))
        {
            os = "Apple";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            os = "Android";
        } else{
            os = "UnKnown";
        }

        if(user.contains("msie") || user.contains("edg"))
        {
            browser = "Internet Explorer";
        }
        else if (user.contains("safari") && (user.contains("mac") || user.contains("iphone") || user.contains("ios")))
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
        } else
        {
            browser = "UnKnown";
        }

        result.add(browser);
        result.add(os);

        return result;
    }
}
