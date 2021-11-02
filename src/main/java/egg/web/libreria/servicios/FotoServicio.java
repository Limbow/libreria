package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Foto;
import egg.web.libreria.errors.ErrorServicio;
import egg.web.libreria.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fR;

    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {
        
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                System.out.println("EL TRY FUNCIONO BIEN");
                
                return fR.save(foto);
                
            } catch (IOException e) {
                System.out.println("ERROR EN EL TRY");
                System.out.println(e.getMessage());
                return null;
            }
        }else{
            System.out.println("ARCHIVO NULL");
            return null;
        }
    }

    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                
                if (idFoto != null) {
                    Optional<Foto> respuesta = fR.findById(idFoto);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }
                }
                
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fR.save(foto);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }
}
