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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Flia Vasquez
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private LibroRepositorio libroRepositorio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(@RequestParam Integer id, ModelMap model, HttpSession session) {

        Usuario login = (Usuario) session.getAttribute("sessionUsuario");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/";
        }

        Usuario usuario = usuarioRepositorio.buscarPorId(id);
        model.addAttribute("perfil", usuario);

        if (usuario.getLibro() != null) {
            Libro libro = libroRepositorio.buscarPorISBN(usuario.getLibro().getISBN());
            model.put("libro", libro);
        }

        return "user.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap model, HttpSession session, MultipartFile profimg, @RequestParam Integer id, String name, String password1, String password2, String email) {

        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("sessionUsuario");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/";
            }
            usuario = usuarioRepositorio.buscarPorId(id);
            usuarioServicio.modificar(profimg, id, name, email, password2, password2);
            session.setAttribute("sessionUsuario", usuario);
            return "redirect:/account";
        } catch (ErrorServicio e) {
            Libro libro = libroRepositorio.buscarPorISBN(usuario.getLibro().getISBN());
            model.put("libro", libro);
            model.put("error", e.getMessage());
            model.addAttribute("perfil", usuario);
        }

        return "user.html";
    }
}
