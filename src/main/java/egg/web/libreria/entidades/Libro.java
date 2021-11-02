/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Flia Vasquez
 */
@Entity
public class Libro implements Serializable {

    @Id
    private Integer ISBN;
    private String titulo;
    private Integer ejemplares;
    private Integer prestados;
    private Integer restantes;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPublicacion;
    @ManyToOne
    private Autor autor;
    @ManyToOne
    private Editorial editorial;

    public Libro() {
        this.prestados = 0;
    }

    public Libro(Integer ISBN, Integer ejemplares, Integer prestados, Integer restantes, Date fechaPublicacion, Autor idAutor, Editorial editorial) {
        this.ISBN = ISBN;
        this.ejemplares = ejemplares;
        this.prestados = prestados;
        this.restantes = restantes;
        this.fechaPublicacion = fechaPublicacion;
        this.autor = idAutor;
        this.editorial = editorial;
    }

    public Integer getISBN() {
        return ISBN;
    }

    public void setISBN(Integer ISBN) {
        this.ISBN = ISBN;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Integer getPrestados() {
        return prestados;
    }

    public void setPrestados(Integer prestados) {
        this.prestados = prestados;
    }

    public Integer getRestantes() {
        return restantes;
    }

    public void setRestantes(Integer restantes) {
        this.restantes = restantes;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    
    
}
