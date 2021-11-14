
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Autor;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Integer> {
    
    @Query("SELECT a FROM Autor a WHERE a.id = :id")
    public Autor buscarPorId(@Param("id") Integer id);
    
    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE '%:nombre%'")
    public ArrayList<Autor> buscarPorNombre(@Param("nombre") String nombre);
    
    public ArrayList<Autor> findByNombreContains(String title);
    
    //Faltan los ABM
}
