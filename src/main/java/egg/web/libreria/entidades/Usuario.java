
package egg.web.libreria.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;


@Entity
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    private String email;
    private String password;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date alta;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date baja;
    
    @OneToOne
    private Libro libro;
    
    @OneToOne
    private Foto foto;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String email, String password, Date alta, Date baja, Libro libro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.alta = alta;
        this.baja = baja;
        this.libro = libro;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    public Date getBaja() {
        return baja;
    }

    public void setBaja(Date baja) {
        this.baja = baja;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
    
    
}

