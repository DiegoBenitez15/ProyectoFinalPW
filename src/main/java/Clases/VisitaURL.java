package Clases;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class VisitaURL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private URL url;
    @Column
    private String navegador;
    @Column
    private String ip;
    @Column
    private String so;
    @Column
    private Date fecha_registro = this.setFecha();

    public VisitaURL() {
    }

    public VisitaURL(URL url,String navegador, String ip, String so) {
        this.url = url;
        this.navegador = navegador;
        this.ip = ip;
        this.so = so;
    }

    public VisitaURL(URL url) {
        this.url = url;
        this.fecha_registro = new Date();
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
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

    public String getFechaString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(this.fecha_registro);
    }
}
