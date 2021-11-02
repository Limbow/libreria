/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Editorial;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.EditorialRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Flia Vasquez
 */
@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio eR;
    
    @Transactional
    public void registrarEditorial(String nombre) throws ErrorServicio {
        validar(nombre);

        Editorial e = new Editorial();
        e.setNombre(nombre);

        eR.save(e);
    }

    @Transactional
    public void modificarEditorial(Integer id, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Editorial> respuesta = eR.findById(id);
        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            e.setNombre(nombre);

            eR.save(e);
        } else{
            throw new ErrorServicio("No se encontro el autor solicitado");
        }

    }

    @Transactional
    public void bajaEditorial(Integer id) throws ErrorServicio{
        Optional<Editorial> respuesta = eR.findById(id);
        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            eR.delete(e);
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
