package egg.web.libreria.controladores;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Item;
import egg.web.libreria.entidades.Autor;
import egg.web.libreria.entidades.Editorial;
import egg.web.libreria.entidades.Libro;
import egg.web.libreria.entidades.Usuario;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.AutorRepositorio;
import egg.web.libreria.repositorios.EditorialRepositorio;
import egg.web.libreria.repositorios.LibroRepositorio;
import egg.web.libreria.servicios.AutorServicio;
import egg.web.libreria.servicios.LibroServicio;
import egg.web.libreria.servicios.PagoServicio;
import egg.web.libreria.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioService;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio ediRepositorio;
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/")
    public String index(ModelMap model) {
        try {
            MercadoPago.SDK.setAccessToken("APP_USR-5937986277032148-101923-5f7e275772736b2c39ac66ce485d6408-277723064");
            // Crea un objeto de preferencia
            Preference preference = new Preference();

            // Crea un ítem en la preferencia
            Item item = new Item();
            item.setTitle("Prueba de pago 1")
                    .setQuantity(1)
                    .setUnitPrice((float) 50.00);
            preference.appendItem(item);
            preference.save();
            model.put("preference", preference.getId());
            model.addAttribute("item", item);
        } catch (MPConfException ex) {
            Logger.getLogger(PagoServicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPException ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/user")
    public String user(ModelMap model) {
        return "user.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/account")
    public String account(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuario");
        
        model.addAttribute("perfil", usuario);
        
        return "account.html";
    }

    @GetMapping("/log-in")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Correo electrocnico o clave incorrectos");
            return "error.html";
        }

        return "log-in.html";
    }

    @GetMapping("/signup")
    public String registro() {
        return "signup.html";
    }

    @GetMapping("/books")
    public String books(ModelMap model) {
        List<Libro> libros = libroRepositorio.findAll();

        List<Autor> autores = autorRepositorio.findAll();

        List<Editorial> editoriales = ediRepositorio.findAll();

        model.put("editoriales", editoriales);

        model.put("autores", autores);

        model.put("books", libros);

        model.put("libros", libros);

        return "books.html";
    }

    @GetMapping("/autores")
    public String autores(ModelMap model) {
        List<Autor> autores = autorRepositorio.findAll();

        model.put("authors", autores);

        return "autores.html";
    }

    @GetMapping("/editoriales")
    public String editoriales(ModelMap model) {
        List<Editorial> editoriales = ediRepositorio.findAll();

        model.put("editoriales", editoriales);

        return "editoriales.html";
    }

    @PostMapping("/search")
    public String search(ModelMap model, String text) {

//        List<Libro> libros = libroRepositorio.buscarPorTitulo(text);
//
//        List<Autor> autores = autorRepositorio.buscarPorNombre(text);
//
//        List<Editorial> editoriales = ediRepositorio.buscarPorNombre(text);
        List<Libro> libros = libroRepositorio.findByTituloContains(text);
        List<Autor> autores = autorRepositorio.findByNombreContains(text);
        List<Editorial> editoriales = ediRepositorio.findByNombreContains(text);

        Boolean resultado = libros.isEmpty() && autores.isEmpty() && editoriales.isEmpty();

        model.put("resultado", resultado);

        model.put("editoriales", editoriales);

        model.put("authors", autores);

        model.put("books", libros);

        //model.put("text",text);
        return "search.html";
    }

    @PostMapping("/books")
    public String cargarlibro(ModelMap modelo, Integer isbn, String title, Integer autor, Integer editorial, Integer ejemplares, @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

        try {
            libroServicio.registrarLibro(isbn, title, ejemplares, fecha, autor, editorial);
        } catch (ErrorServicio ex) {
            modelo.put("titulo", "Error durante la registracion del libro");
            modelo.put("desc", "Compruebe que los datos hayan sido los correctos e intentelo denuevo");
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "error.html";
        }

        modelo.put("titulo", "El libro fue registrado exitosamente");

        return "succes.html";
    }

    @PostMapping("/signup")
    public String registrar(ModelMap modelo, MultipartFile profimg, @RequestParam String name, @RequestParam String password, @RequestParam String password2, @RequestParam String email) {

        try {
            usuarioService.registrarUsuario(profimg, name, email, password, password2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("name", name);
            modelo.put("password", password);
            modelo.put("password2", password2);
            modelo.put("email", email);
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "signup.html";
        }
        modelo.put("titulo", "Bienvenido a la Libreria Online");
        modelo.put("desc", "Tu usuario fue registrado de manera satisfactioria");
        return "succes.html";
    }

    @PostMapping("/autores")
    public String agregarAutor(String nombre, HttpSession session, ModelMap model) {

        try {
            autorServicio.registrarAutor(nombre);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "autores.html";
        }

        model.put("titulo", "Autor agregado correctamente");
        return "succes.html";
    }

}
