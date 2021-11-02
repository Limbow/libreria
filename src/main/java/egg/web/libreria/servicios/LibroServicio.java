package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Autor;
import egg.web.libreria.entidades.Editorial;
import egg.web.libreria.entidades.Libro;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.AutorRepositorio;
import egg.web.libreria.repositorios.EditorialRepositorio;
import egg.web.libreria.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio repo;
    @Autowired
    private AutorRepositorio repoAutor;
    @Autowired
    private EditorialRepositorio repoEditorial;

    @Transactional
    public void registrarLibro(Integer isbn, String titulo, Integer ejemplares, Date fechapublicacion, Integer idAutor, Integer idEdi) throws ErrorServicio {

        Autor autor = repoAutor.buscarPorId(idAutor);
        Editorial editorial = repoEditorial.buscarPorId(idEdi);
        validar(isbn, titulo, autor, editorial);

        Libro libro = new Libro();

        libro.setISBN(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setPrestados(0);
        libro.setRestantes(ejemplares);
        libro.setFechaPublicacion(fechapublicacion);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        repo.save(libro);
    }

    @Transactional
    public void modificarLibro(Integer isbn, String titulo, Integer ejemplares, Date fechapublicacion, Integer idAutor, Integer idEditorial) throws ErrorServicio {

        Optional<Libro> respuesta = repo.findById(isbn);
        if (respuesta.isPresent()) {
            Autor autor = repoAutor.findById(idAutor).get();
            Editorial editorial = repoEditorial.findById(idEditorial).get();
            validar(isbn, titulo, autor, editorial);
            Libro libro = respuesta.get();
            if (ejemplares < libro.getPrestados() || ejemplares > libro.getRestantes()) {
                throw new ErrorServicio("El numero de ejemplares es incorrecto ");
            }
            libro.setISBN(isbn);
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setFechaPublicacion(fechapublicacion);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            repo.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }

    }

    @Transactional
    public void bajaLibro(Integer isbn) throws ErrorServicio {

        Optional<Libro> respuesta = repo.findById(isbn);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            repo.delete(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }
    }

    @Transactional
    public void prestarLibro(Integer isbn) throws ErrorServicio {

        Optional<Libro> respuesta = repo.findById(isbn);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getRestantes() == 0) {
                throw new ErrorServicio("No hay libros para prestar");
            } else {
                libro.setPrestados(libro.getPrestados() + 1);
                libro.setRestantes(libro.getEjemplares() - libro.getPrestados());
                repo.save(libro);
            }
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }

    }

    @Transactional
    public void devolverLibro(Integer isbn) throws ErrorServicio {

        Optional<Libro> respuesta = repo.findById(isbn);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getRestantes().equals(libro.getEjemplares())) {
                throw new ErrorServicio("No hay libros prestados con este ISBN");
            } else {
                libro.setPrestados(libro.getPrestados() - 1);
                libro.setRestantes(libro.getEjemplares() - libro.getPrestados());
                repo.save(libro);
            }
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado");
        }
    }

    public void validar(Integer isbn, String titulo, Autor autor, Editorial editorial) throws ErrorServicio {
        if (isbn == null || isbn <= 0) {
            throw new ErrorServicio("El ISBN es incorrecto");
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo no puede estar vacio");
        }

        if (autor == null) {
            throw new ErrorServicio("Autor incorrecto");
        }

        if (editorial == null) {
            throw new ErrorServicio("Editorial incorrecta");
        }
    }
}
