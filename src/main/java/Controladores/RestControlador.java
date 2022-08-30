package Controladores;

import Clases.Respuesta;
import Controladores.UrlWebServices;
import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import Clases.URL;
import Clases.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import Servicios.ServicioURL;
import Servicios.ServicioUsuario;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RestControlador {
    private Javalin app;
    String SECRET_KEY = "asd12D1234dfr123@#4Fsdcasdd5g78a";
    private final ServicioUsuario usuarioServicios = ServicioUsuario.getInstancia();
    private final ServicioURL urlServicios = ServicioURL.getInstancia();
    private final UrlWebServices uws = new UrlWebServices();

    private Usuario user = new Usuario();

    public RestControlador(Javalin app){
        this.app = app;
    }

    public void aplicarRuta(){

        app.routes(() -> {

            path("/api", () -> {

                before("/usuarios/*", ctx -> {
                    String header = "Authorization";
                    String prefix = "Bearer";

                    if(ctx.req.getHeader(header) == null || !ctx.req.getHeader(header).startsWith(prefix)){
                        ctx.res.sendError(401, "You don't have the authorization token");
                    }

                    String jwt = ctx.req.getHeader(header).replace(prefix, "");

                    try {
                        Claims claims = Jwts.parser()
                                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                                .parseClaimsJws(jwt).getBody();
                        System.out.println("Mostrando el JWT recibido: " + claims.toString());
                    }catch (ExpiredJwtException | MalformedJwtException | SignatureException e){
                        ctx.res.sendError(403, "You don't have permission to access this API, error: "+e.getMessage());
                    }

                    System.out.println(jwt);

                    ctx.result(jwt);
                });

                post("/login", ctx -> {
                    //crear objeto json
                    JSONObject jsonObject = new JSONObject();

                    try{

                        jsonObject = new JSONObject(ctx.body());
                        System.out.println(jsonObject.get("usuario"));
                    }catch(JSONException e){
                        ctx.res.sendError(400,"You have to send a JSON with the credentials");
                    }

                    String usuario = jsonObject.getString("usuario");
                    String pass = jsonObject.getString("password");

                    user = usuarioServicios.autenticar(usuario, pass);

                    if(user != null){
                        ctx.json(generateToken(usuario));
                    }else{
                        ctx.json("Error: Nombre de usuario o contraseña incorrectos.");
                    }
                });


                //listar urls por usuario seleccionado
                get("/usuarios/{username}", ctx -> {
                    String username = ctx.pathParam("username");
                    List<URL> urls = uws.listarURL(username);


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user", username);
                    jsonObject.put("urls", "No tiene urls...");

                    if(urls.size() > 0){
                        jsonObject.remove("urls");

                        for (Object url: urls ) {
                            jsonObject.accumulate("urls", new JSONObject(url));
                        }
                    }

                    ctx.result(jsonObject.toString());
                });


                //Crear short url para un usuario
                post("/usuarios/{username}", ctx -> {

                    String username = ctx.pathParam("username");
                    String link = ctx.queryParam("url");
                    String shortLink = uws.crearShortUrl(link,username);


                    JSONObject jsonObject = new JSONObject(shortLink);

                    jsonObject.put("user", username);

                    ctx.result(jsonObject.toString());
                });
            });
        });
    }

    public Respuesta generateToken(String usuario){
        Respuesta respuesta = new Respuesta();
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);
        System.out.println("La fecha actual es: "+ localDateTime);

        Date fechaExpiracion = Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4)));

        String jwt = Jwts.builder()
                .setIssuer("Web App")
                .setSubject("AcortadorURL")
                .setExpiration(fechaExpiracion)
                .claim("usuario", usuario)
                .signWith(secretKey)
                .compact();

        respuesta.setToken(jwt);
        respuesta.setExpiracion(fechaExpiracion.getTime());

        return respuesta;
    }

}