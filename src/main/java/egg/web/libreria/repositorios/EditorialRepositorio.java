
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {
    
    @Query("SELECT e FROM Editorial e  WHERE e.id = :id")
    public Editorial buscarPorId(@Param("id") Integer id);
    
    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarPorNombre(@Param("nombre") String nombre);
    
    //Faltan los ABM
}
