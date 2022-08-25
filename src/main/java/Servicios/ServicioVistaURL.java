package Servicios;

import Clases.VisitaURL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ServicioVistaURL extends GestionDb<VisitaURL>{
    private static ServicioVistaURL instancia;

    private ServicioVistaURL(){
        super(VisitaURL.class);
    }

    public static ServicioVistaURL getInstancia(){
        if(instancia==null){
            instancia = new ServicioVistaURL();
        }
        return instancia;
    }

    public int getClicksTotal(int id){
        EntityManager em = getEntityManager();
        List<VisitaURL> urls;

        try {
            urls = em.createQuery("SELECT c FROM VisitaURL c WHERE c.url LIKE " + id, VisitaURL.class).getResultList();
        }catch (NoResultException e){
            urls = new ArrayList<>();
        }

        return urls.size();
    }

    public int getClicksHoy(int id){
        EntityManager em = getEntityManager();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date();
        List<VisitaURL> urls;

        try {
            urls = em.createQuery("SELECT c FROM VisitaURL c WHERE c.url LIKE " + id + " and CAST(c.fecha_registro AS date) = " + "'" + dateFormat.format(fecha)  + "'", VisitaURL.class).getResultList();
        }catch (NoResultException e){
            urls = new ArrayList<>();
        }

        return urls.size();
    }

    public List<Integer> getBrowsers(int id){
        EntityManager em = getEntityManager();
        List<String> browser = Arrays.asList("Chrome", "Firefox", "Opera", "Internet Explorer", "Safari","UnKnown");
        List<Integer> resultado = new ArrayList<>();
        for(int i = 0;i < browser.size();i++) {
            List<VisitaURL> urls;
            try {
                urls = em.createQuery("SELECT c FROM VisitaURL c WHERE c.url LIKE " + id + "and c.navegador =" + "'" + browser.get(i) + "'", VisitaURL.class).getResultList();
            } catch (NoResultException e) {
                urls = new ArrayList<>();
            }
            resultado.add(urls.size());
        }

        return resultado;
    }

    public List<Integer> getSO(int id){
        EntityManager em = getEntityManager();
        List<String> browser = Arrays.asList("Windows", "Apple", "Unix","Android","UnKnown");
        List<Integer> resultado = new ArrayList<>();
        for(int i = 0;i < browser.size();i++) {
            List<VisitaURL> urls;
            try {
                urls = em.createQuery("SELECT c FROM VisitaURL c WHERE c.url LIKE " + id + "and c.so =" + "'" + browser.get(i) + "'", VisitaURL.class).getResultList();
            } catch (NoResultException e) {
                urls = new ArrayList<>();
            }
            resultado.add(urls.size());
        }

        return resultado;
    }

    public List<Integer> getClicksHours(int id,String fecha){
        EntityManager em = getEntityManager();
        List<Integer> resultado = new ArrayList<>();

        for(int i=0;i<24;i++){
            List<VisitaURL> urls;
            try {
                urls = em.createQuery("SELECT c FROM VisitaURL c WHERE c.url LIKE " + id + " and CAST(c.fecha_registro AS date) = " + "'" + fecha  + "' and HOUR(c.fecha_registro) = " + "'" + i + "'", VisitaURL.class).getResultList();
            }catch (NoResultException e){
                urls = new ArrayList<>();
            }
            resultado.add(urls.size());
        }

        return resultado;
    }
}
