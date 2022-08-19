package Clases;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {
    @Id
    private String usuario;
    @Column
    private String password;
    @Column
    private Boolean active;
    @OneToOne
    private Roles role;

    public Usuario(){}

    public Usuario(String usuario, String password,Roles role) {
        this.usuario = usuario;
        this.password = password;
        this.role = role;
        this.active = Boolean.TRUE;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
