package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Autor;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.AutorRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio aR;

    @Transactional
    public void registrarAutor(String nombre) throws ErrorServicio {
        validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);

        aR.save(autor);
    }

    @Transactional
    public void modificarAutor(Integer id, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Autor> respuesta = aR.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);

            aR.save(autor);
        } else{
            throw new ErrorServicio("No se encontro el autor solicitado");
        }

    }

    @Transactional
    public void bajaAutor(Integer id) throws ErrorServicio{
        Optional<Autor> respuesta = aR.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            aR.delete(autor);
        } else{
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }
    
    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede esar vacio");
        }
    }
}
