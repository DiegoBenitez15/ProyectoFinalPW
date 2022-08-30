package Controladores;

import Clases.Imagen;
import Clases.URL;
import Clases.UrlEstadisticas;
import Clases.Usuario;
import Servicios.ServicioURL;
import Servicios.ServicioUsuario;
import Servicios.ServicioVistaURL;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@WebService
public class UrlWebServices {
    private ServicioURL surl = ServicioURL.getInstancia();
    private ServicioUsuario su = ServicioUsuario.getInstancia();
    private ServicioVistaURL svurl = ServicioVistaURL.getInstancia();
    private Imagen img = Imagen.getInstancia();

    @WebMethod
    public URL getbyShortUrl(String ShortUrl){
        return surl.findShortUrl(ShortUrl);
    }

    public URL getbyLongUrl(String ShortUrl){
        return surl.findShortUrl(ShortUrl);
    }

    @WebMethod
    public List<URL> listarURL(String usuario){
        Usuario u = su.find(usuario);
        List<URL> urls = new ArrayList<URL>();

        if(u != null){
            urls = surl.findByUser(u.getUsuario(),String.valueOf(u.getRole().getId()));
        }

        return urls;
    }

    @WebMethod
    public String getListadoURLEstadisticas(String usuario,String fecha){
        List<URL> urls = this.listarURL(usuario);
        JSONArray result = new JSONArray();

        if(su.find(usuario) == null){
            return "ERROR";
        }

        for(URL url:urls){
            JSONObject temp = new JSONObject();
            temp.put("longUrl",url.getLongUrl());
            temp.put("shortUrl",url.getShortURL());
            temp.put("usuario",url.getUsuario());
            temp.put("fecha",url.getFecha());
            temp.put("total_clicks",svurl.getClicksTotal((int) url.getId()));
            temp.put("hoy_clicks",svurl.getClicksHoy((int) url.getId()));
            temp.put("browser_clicks",svurl.getBroserWS((int) url.getId()));
            temp.put("so_clicks",svurl.getSoWS((int) url.getId()));
            temp.put("hour_clicks",svurl.getClicksHoursWS((int) url.getId(),fecha));
            result.put(temp);
        }

        return result.toString();
    }

    public String getURLEstadisticas(String shortUrl){
        URL url = this.getbyShortUrl(shortUrl);
        JSONObject result = new JSONObject();

        result.put("longUrl",url.getLongUrl());
        result.put("shortUrl",url.getShortURL());
        result.put("fecha",url.getFecha());
        result.put("total_clicks",svurl.getClicksTotal((int) url.getId()));
        result.put("hoy_clicks",svurl.getClicksHoy((int) url.getId()));
        result.put("browser_clicks",svurl.getBroserWS((int) url.getId()));
        result.put("so_clicks",svurl.getSoWS((int) url.getId()));
        result.put("hour_clicks",svurl.getClicksHoursWS((int) url.getId(),url.getFechaString()));
        result.put("image",img.getByteArrayFromImageURL(url.getLongUrl()));


        return result.toString();
    }

    @WebMethod
    public String crearShortUrl(String longUrl,String usuario){

        if(su.find(usuario) == null){
            return "ERROR";
        }

        String shortUrl = surl.createShortLink(longUrl,usuario);

        if(Objects.equals(shortUrl, "STATUS:404")){
            return "ERROR";
        }

        return this.getURLEstadisticas(shortUrl);
    }

}
