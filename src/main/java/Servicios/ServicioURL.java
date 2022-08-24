package Servicios;

import Clases.URL;
import Clases.Usuario;
import Clases.shortenerURL;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.apache.commons.validator.routines.UrlValidator;
import org.xbill.DNS.*;


public class ServicioURL extends GestionDb<URL>{
    private final shortenerURL client = new shortenerURL();
    private static ServicioURL instancia;

    private ServicioURL(){
        super(URL.class);
    }

    public static ServicioURL getInstancia(){
        if(instancia==null){
            instancia = new ServicioURL();
        }
        return instancia;
    }

    public String createShortLink(String longUrl,Usuario usuario){
        UrlValidator defaultValidator = new UrlValidator();

        if(defaultValidator.isValid(longUrl)){
            URL url = null;
            if(usuario != null) {
                url = this.findLongUrl(longUrl, usuario.getUsuario());
            }
            if (url == null || url.getActive() == Boolean.FALSE) {

                if(url !=null){
                    url.setShortURL(null);
                    this.editar(url);
                }

                url = this.crear(new URL());
                url.setLongUrl(longUrl);
                url.setShortURL(client.encode(url.getId()));
                url.setUsuario(usuario);
                this.editar(url);
            }

            return url.getShortURL();
        }

        return "STATUS:404";
    }

    public List<URL> findByUser(String usuario,String role){
        EntityManager em = getEntityManager();
        List<URL> urls;

        try {
            if(Objects.equals(role, "1")) {
                urls = em.createQuery("SELECT c FROM URL c WHERE c.active = TRUE", URL.class).getResultList();
            } else {
                urls = em.createQuery("SELECT c FROM URL c WHERE c.usuario LIKE " + "'" + usuario + "'" + " and c.active = TRUE", URL.class).getResultList();
            }
        }catch (NoResultException e){
            urls = null;
        }

        return urls;

    }

    public URL findShortUrl(String shortUrl){
        EntityManager em = getEntityManager();
        URL url;

        try {
            url = em.createQuery("SELECT c FROM URL c WHERE c.shortURL LIKE " + "'" + shortUrl + "'" + " and c.active = True", URL.class).getSingleResult();
        }catch (NoResultException e){
            url = null;
        }

        return url;
    }

    public URL findLongUrl(String longUrl,String usuario){
        EntityManager em = getEntityManager();
        URL url;
        try {
            url = em.createQuery("SELECT c FROM URL c WHERE c.longUrl LIKE " + "'" + longUrl + "'" + " and c.usuario LIKE " + "'" +usuario + "' and c.active = True", URL.class).getSingleResult();
        }catch (NoResultException e){
            url = null;
        }

        return url;
    }
}
