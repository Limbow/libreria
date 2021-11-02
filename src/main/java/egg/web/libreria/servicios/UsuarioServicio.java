package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Foto;
import egg.web.libreria.entidades.Libro;
import egg.web.libreria.entidades.Usuario;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.LibroRepositorio;
import egg.web.libreria.repositorios.UsuarioRepositorio;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio uR;
    @Autowired
    private LibroRepositorio repoLibro;
    @Autowired
    private LibroServicio libroS;
    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombre, String email, String password, String password2) throws ErrorServicio {
        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        String encriptada = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encriptada);

        usuario.setAlta(new Date());

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        uR.save(usuario);
    }

    @Transactional
    public void modificar(MultipartFile archivo, Integer id, String nombre, String email, String password, String password2) throws ErrorServicio {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            String encriptada = new BCryptPasswordEncoder().encode(password);
            usuario.setPassword(encriptada);

            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);

            uR.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }

    }

    @Transactional
    public void bajaUser(Integer id) throws ErrorServicio {

        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());

            uR.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void altaUser(Integer id) throws ErrorServicio {

        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setAlta(new Date());
            usuario.setBaja(null);
            uR.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void pedirPrestado(Integer id, Integer isbnLibro) throws ErrorServicio {

        Optional<Libro> respuesta = repoLibro.findById(isbnLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();

            Optional<Usuario> resUser = uR.findById(id);
            if (resUser.isPresent()) {
                Usuario usuario = resUser.get();
                if (usuario.getLibro() != null) {
                    throw new ErrorServicio("Ya tiene un libro");
                } else {
                    usuario.setLibro(libro);
                    libroS.prestarLibro(libro.getISBN());
                    uR.save(usuario);
                }
            } else {
                throw new ErrorServicio("Error con el usuario. Verifique los datos");
            }
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }
    }

    @Transactional
    public void devolverLibro(Integer id, Integer isbnLibro) throws ErrorServicio {
        Libro libro = repoLibro.buscarPorISBN(isbnLibro);
        if (libro != null) {
            libroS.devolverLibro(libro.getISBN());
            Usuario usuario = uR.buscarPorId(id);
            usuario.setLibro(null);
            uR.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }

    }

    public void validar(String nombre, String email, String password, String password2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede estar vacio");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("El email no puede estar vacio");
        }
        if (password == null || password.isEmpty() || password.length() <= 6) {
            throw new ErrorServicio("La contraseña no puede estar vacia y debe tener mas de 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new ErrorServicio("Las contraseñas no coinciden");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = uR.buscarPorMail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_LIBROS");
            permisos.add(p1);

            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_AUTORES");
            permisos.add(p2);

            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_EDITORIALES");
            permisos.add(p3);

            GrantedAuthority p4 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p4);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("sessionUsuario", usuario);

            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);
            return user;
        } else {
            return null;
        }

    }
}
