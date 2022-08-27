package Clases;

import java.util.Date;
import java.util.HashMap;

public class UrlEstadisticas{
    private String longUrl;
    private String shortURL;
    private String usuario;
    private Date fecha_creacion;
    private int clicks_totales;
    private int clicks_hoy;
    private HashMap<String, Integer> clicks_browser;
    private HashMap<String, Integer> clicks_so;

    private HashMap<String, Integer> clicksHours;

    public UrlEstadisticas(String longUrl, String shortURL, String usuario, Date fecha_creacion, int clicks_totales, int clicks_hoy, HashMap<String, Integer> clicks_browser, HashMap<String, Integer> clicks_so, HashMap<String, Integer> clicksHours) {
        this.longUrl = longUrl;
        this.shortURL = shortURL;
        this.usuario = usuario;
        this.fecha_creacion = fecha_creacion;
        this.clicks_totales = clicks_totales;
        this.clicks_hoy = clicks_hoy;
        this.clicks_browser =clicks_browser;
        this.clicks_so = clicks_so;
        this.clicksHours =clicksHours;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getClicks_totales() {
        return clicks_totales;
    }

    public void setClicks_totales(int clicks_totales) {
        this.clicks_totales = clicks_totales;
    }

    public int getClicks_hoy() {
        return clicks_hoy;
    }

    public void setClicks_hoy(int clicks_hoy) {
        this.clicks_hoy = clicks_hoy;
    }
}
