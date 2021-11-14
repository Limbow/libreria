/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Usuario;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.UsuarioRepositorio;
import egg.web.libreria.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Joaquin
 */
@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUusuerio(@PathVariable Integer id) throws ErrorServicio {

        Usuario usuario = usuarioRepositorio.buscarPorId(id);
        
        if (usuario.getFoto() == null) {
            throw new ErrorServicio("El usuario no tiene una foto");
        }
        
        byte[] foto = usuario.getFoto().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(foto, headers, HttpStatus.OK);
    }

}
