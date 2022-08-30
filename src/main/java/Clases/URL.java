package Clases;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Entity
public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String longUrl;
    @Column
    private String shortURL;
    @Column
    private String usuario;
    @Column
    private Date fecha_creacion = this.setFecha();
    @Column
    private Boolean active = Boolean.TRUE;

    public URL() {
    }

    public URL(String longUrl, String shortURL,String usuario) {
        this.longUrl = longUrl;
        this.shortURL = shortURL;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getFecha() {
        return fecha_creacion;
    }

    public String getFechaString(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return dateFormat.format(this.fecha_creacion);
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(this.fecha_creacion);
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date setFecha(){
        Date hoy = new Date();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("America/Caracas"));
        String hoy_string = isoFormat.format(hoy);
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(hoy_string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
