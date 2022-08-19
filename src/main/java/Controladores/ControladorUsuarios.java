package Controladores;

import Clases.Roles;
import Clases.Usuario;
import Servicios.ServicioRole;
import Servicios.ServicioUsuario;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControladorUsuarios {
    private final Javalin app;
    private final ServicioUsuario su = ServicioUsuario.getInstancia();
    private final ServicioRole sr = ServicioRole.getInstancia();

    public ControladorUsuarios(Javalin app) {
        this.app = app;
        sr.crear(new Roles("Administrador"));
        sr.crear(new Roles("Usuarios"));
        su.crear(new Usuario("admin","admin",sr.find(1)));
        su.crear(new Usuario("user","user",sr.find(2)));
    }

    public void aplicarRuta(){
        app.routes(() -> {
            path("/login",() -> {
                get("/",ctx -> {
                    ctx.render("templates/login.html");
                });
                post("/",ctx -> {
                    String usuario = ctx.formParam("user");
                    String password = ctx.formParam("pass");

                    if(!Objects.equals(usuario, "") && !Objects.equals(password, "")) {
                        Usuario u = su.find(usuario);

                        if (u != null && Objects.equals(u.getPassword(), password)) {
                            ctx.cookie("usuario",usuario);
                            ctx.cookie("role",String.valueOf(su.find(usuario).getRole().getId()));

                            ctx.redirect("/inicio");
                        }
                        else{
                            ctx.redirect("/login");
                        }
                    }
                    else{
                        ctx.redirect("/login");
                    }
                });
            });
            path("/registrar",() -> {
                get("/",ctx -> {
                    ctx.render("templates/register.html");
                });
                post("/",ctx -> {
                    String usuario = ctx.formParam("user");
                    String password = ctx.formParam("pass");

                    if(!Objects.equals(usuario, "") && !Objects.equals(password, "")) {
                        Usuario u = new Usuario(usuario,password,sr.find(1));

                        if(su.find(usuario) == null){
                            su.crear(u);
                            ctx.redirect("/login");
                        }
                        else{
                            ctx.redirect("/registrar");
                        }
                    }
                    else{
                        ctx.redirect("/registrar");
                    }
                });
            });
            path("/usuarios",() -> {
                get("/administrar",ctx -> {
                    String pag = ctx.queryParam("pag");

                    if (pag == null || Integer.parseInt(pag) < 0){
                        pag = "0";
                    }
                    else if (Integer.parseInt(pag) > su.getActivos()){
                        pag = String.valueOf(Integer.parseInt(pag) - 5);
                    }

                    HashMap<String,Object> modelo = new HashMap<String,Object>();
                    modelo.put("usuarios",su.findAllPag(Integer.parseInt(pag)));
                    modelo.put("usuario",ctx.cookie("usuario"));
                    modelo.put("role",ctx.cookie("role"));
                    modelo.put("pag",Integer.parseInt(pag));
                    ctx.render("/templates/listado_usuarios.html",modelo);
                });
                get("/eliminar",ctx -> {
                    String usuario = ctx.queryParam("usuario");
                    Usuario user = su.find(usuario);
                    user.setActive(Boolean.FALSE);
                    su.editar(user);
                    ctx.redirect("/usuarios/administrar");
                });
                post("/cambiar/role",ctx -> {
                    String usuario = ctx.queryParam("usuario");
                    String role = ctx.formParam("role");
                    Usuario user = su.find(usuario);
                    user.setRole(sr.find(role));
                    su.editar(user);
                    ctx.redirect("/usuarios/administrar");
                });
            });
            app.get("/logout",ctx -> {
                ctx.cookie("usuario","",0);
                ctx.cookie("role","",0);
                ctx.redirect("/inicio");
            });
        });
    }
}
