/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Libro;
import egg.web.libreria.entidades.Usuario;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.LibroRepositorio;
import egg.web.libreria.repositorios.UsuarioRepositorio;
import egg.web.libreria.servicios.LibroServicio;
import egg.web.libreria.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Flia Vasquez
 */
@Controller
@RequestMapping("")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/prestarLibro")
    public String prestar(@RequestParam Integer libro, HttpSession session, ModelMap model) throws ErrorServicio {
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuario");

        try {
            System.out.println(usuario.getId());
            if (usuario.getLibro() != null) {
                throw new ErrorServicio("Ya tiene un libro en sus manos");
            } else {
                usuarioServicio.pedirPrestado(usuario.getId(), libro);
            }
        } catch (ErrorServicio e) {
            model.put("titulo", e.getMessage());
            return "succes.html";
        }

        model.put("titulo", "Libro prestado");
        model.put("desc", "El libro esta en tus manos, cuidalo bien!");

        return "succes.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/devolver-libro")
    public String devolver(@RequestParam Integer id, HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("sessionUsuario");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/";
        }

        Usuario usuario = usuarioRepositorio.buscarPorId(id);

        try {
            usuarioServicio.devolverLibro(id, usuario.getLibro().getISBN());
        } catch (ErrorServicio e) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA" + e.getMessage());
        }

        model.put("titulo", "Libro devuelto");
        model.put("desc", "El libro ha sido devuelto satisfactoriamente. Prueba llevando otro!");
        return "succes.html";
    }
}
