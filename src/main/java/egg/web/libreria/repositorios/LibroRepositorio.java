/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Libro;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Integer>{
    
    @Query("SELECT l FROM Libro l WHERE l.ISBN = :isbn")
    public Libro buscarPorISBN(@Param("isbn") Integer isbn);
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = '%:titulo%'")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM Libro l, Autor a WHERE l.autor.id = :idAutor")
    public ArrayList<Libro> buscarPorAutor(@Param("idAutor")Integer idAutor);
    
    @Query("SELECT l FROM Libro l, Editorial e WHERE l.editorial.id = :idEditorial")
    public ArrayList<Libro> buscarPorEditorial(@Param("idEditorial")Integer idEditorial);
    //Faltan los ABM
}
