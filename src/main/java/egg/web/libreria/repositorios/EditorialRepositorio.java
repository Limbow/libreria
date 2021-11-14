
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Editorial;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {
    
    @Query("SELECT e FROM Editorial e  WHERE e.id = :id")
    public Editorial buscarPorId(@Param("id") Integer id);
    
    @Query("SELECT e FROM Editorial e WHERE e.nombre LIKE '%:nombre%'")
    public ArrayList<Editorial> buscarPorNombre(@Param("nombre") String nombre);
    
    public ArrayList<Editorial> findByNombreContains(String title);
    
    //Faltan los ABM
}
