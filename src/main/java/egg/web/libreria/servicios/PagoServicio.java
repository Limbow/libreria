/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Item;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Joaquin
 */
@Service
public class PagoServicio {

    public void pagar() throws MPException {
        try {
            MercadoPago.SDK.setAccessToken("TEST-5937986277032148-101923-b35b3a307183035d2927e473185484ed-277723064");
            // Crea un objeto de preferencia
            Preference preference = new Preference();

            // Crea un ítem en la preferencia
            Item item = new Item();
            item.setTitle("Mi producto")
                    .setQuantity(1)
                    .setUnitPrice((float) 75.56);
            preference.appendItem(item);
            preference.save();
        } catch (MPConfException ex) {
            Logger.getLogger(PagoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
