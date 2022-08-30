package Servicios;

import Clases.Imagen;
import Clases.URL;
import Clases.Usuario;
import Clases.VisitaURL;
import Excepciones.NoExisteUsuario;
import io.grpc.stub.StreamObserver;
import urlrn.UrlRnGrpc;
import urlrn.UrlRnOuterClass;

import java.util.ArrayList;
import java.util.List;

public class UrlServicesImpl extends UrlRnGrpc.UrlRnImplBase {
    private ServicioURL surl = ServicioURL.getInstancia();
    private ServicioVistaURL svurl = ServicioVistaURL.getInstancia();
    private ServicioUsuario su = ServicioUsuario.getInstancia();
    private Imagen img = Imagen.getInstancia();

    @Override
    public void getUrl(UrlRnOuterClass.UrlRequest request, StreamObserver<UrlRnOuterClass.UrlResponse> responseObserver){
        URL url = surl.findShortUrl(request.getShortUrl());
        if(url!=null){
            responseObserver.onNext(convertirGetUrl(url));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createUrl(urlrn.UrlRnOuterClass.CrearUrlRequest request, StreamObserver<urlrn.UrlRnOuterClass.CrearUrlResponse> responseObserver) {
        Usuario usuario = su.find(request.getUsuario());

        if(usuario == null){
            responseObserver.onError(new NoExisteUsuario("No existe el usuario: "+request.getUsuario()));
        } else {
            String shorturl_string = surl.createShortLink(request.getLongUrl(),usuario.getUsuario());
            URL url = surl.findShortUrl(shorturl_string);
            if(url!=null){
                responseObserver.onNext(convertirCrearURL(url));
                responseObserver.onCompleted();
            }
        }
    }

    @Override
    public void listarUrl(urlrn.UrlRnOuterClass.listarUrlsRequest request,StreamObserver<urlrn.UrlRnOuterClass.listarUrlsResponse> responseObserver) {
        Usuario usuario = su.find(request.getUsuario());

        if(usuario == null){
            responseObserver.onError(new NoExisteUsuario("No existe el usuario: "+request.getUsuario()));
        }else {
            List<URL> urls = surl.findByUser(usuario.getUsuario(),String.valueOf(usuario.getRole().getId()));
            List<UrlRnOuterClass.CrearListarUrlsResponse> urlrn = new ArrayList<UrlRnOuterClass.CrearListarUrlsResponse>();

            if(urls!=null){
                for(URL url:urls){
                    urlrn.add(this.convertirListarCrearURL(url));
                }
                responseObserver.onNext(UrlRnOuterClass.listarUrlsResponse.newBuilder().addAllUrl(urlrn).build());
                responseObserver.onCompleted();
            }
        }
    }



    public UrlRnOuterClass.CrearListarUrlsResponse convertirListarCrearURL(URL url){
        return UrlRnOuterClass.CrearListarUrlsResponse.newBuilder()
                .setLongUrl(url.getLongUrl())
                .setShortUrl(url.getShortURL())
                .setFecha(url.getFechaString())
                .setClickTotal(String.valueOf(svurl.getClicksTotal((int) url.getId())))
                .setClickHoy(String.valueOf(svurl.getClicksHoy((int) url.getId())))
                .putAllBrowserClicks(svurl.getBroserWS((int) url.getId()))
                .putAllSoClicks(svurl.getSoWS((int) url.getId()))
                .putAllHoursClicks(svurl.getClicksHoursWS((int) url.getId(),url.getDateString()))
                .build();
    }

    public UrlRnOuterClass.CrearUrlResponse convertirCrearURL(URL url){
        return UrlRnOuterClass.CrearUrlResponse.newBuilder()
                .setLongUrl(url.getLongUrl())
                .setShortUrl(url.getShortURL())
                .setFecha(url.getFechaString())
                .setClickTotal(String.valueOf(svurl.getClicksTotal((int) url.getId())))
                .setClickHoy(String.valueOf(svurl.getClicksHoy((int) url.getId())))
                .setImage(img.getByteArrayFromImageURL(url.getLongUrl()))
                .putAllBrowserClicks(svurl.getBroserWS((int) url.getId()))
                .putAllSoClicks(svurl.getSoWS((int) url.getId()))
                .putAllHoursClicks(svurl.getClicksHoursWS((int) url.getId(),url.getDateString()))
                .build();
    }

    private UrlRnOuterClass.UrlResponse convertirGetUrl(URL url){
        return UrlRnOuterClass.UrlResponse.newBuilder()
                .setShortUrl(url.getShortURL())
                .setLongUrl(url.getLongUrl())
                .setFecha(url.getFechaString())
                .setUsuario(url.getUsuario())
                .build();
    }
}
