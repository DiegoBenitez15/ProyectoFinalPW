package Servicios;

import Clases.URL;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class GestionDb<T> {

    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;

    public GestionDb(Class<T> claseEntidad) {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("application");
        }
        this.claseEntidad = claseEntidad;
    }

    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    private Object getValorCampo(T entidad){
        if(entidad == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entidad.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) { //preguntando por la anotación ID.
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);

                    System.out.println("Nombre del campo: "+f.getName());
                    System.out.println("Tipo del campo: "+f.getType().getName());
                    System.out.println("Valor del campo: "+valorCampo );

                    return valorCampo;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();

        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidad
     */
    public T editar(T entidad) throws PersistenceException{
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidadId
     */
    public boolean eliminar(Object entidadId) throws PersistenceException{
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }

    public T findURL(String shortUrl){
        EntityManager em = getEntityManager();
        T url;
        try {
            url = em.createQuery("SELECT c FROM URL c WHERE c.shortURL LIKE " + shortUrl, claseEntidad).getSingleResult();
        }catch (NoResultException e){
            url = null;
        }

        return url;
    }

    /**
     *
     * @return
     */
    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }

    public int getActivos(){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        List<T> forms = em.createQuery("From " + claseEntidad.getName(), claseEntidad).getResultList();
        em.getTransaction().commit();
        em.close();
        return forms.size();
    }

    public List<T> findAllPag(int page) throws PersistenceException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Query usuarios = em.createQuery("From Usuario u Where u.active = True", claseEntidad);
        usuarios.setFirstResult(page);
        usuarios.setMaxResults(page + 5);
        return usuarios.getResultList();
    }
}