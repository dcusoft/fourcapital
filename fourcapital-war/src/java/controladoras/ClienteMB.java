/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladoras;

import clienteRest.ClienteREST;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionHTTP;
import entities.Cliente;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.faces.event.ActionEvent;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Usuario
 */
@Named(value = "clienteMB")
@SessionScoped
public class ClienteMB implements Serializable {

    /**
     * Creates a new instance of ClienteMB
     */
    public ClienteMB() {
    }
    
   public void listcliente(ActionEvent actionEvent){
       
       //coneccion por medio de HTTP
       ConexionHTTP connet=new ConexionHTTP();
       List<Cliente> clientes;
       Type type=new TypeToken<List<Cliente>>(){}.getType();
       clientes=connet.conectarServicio("http://localhost:8080/fourcapitalservice/webresources/entidades.cliente", "",type);
       System.out.println(clientes.size());
       
       
       // coneccion por medio de cliente del asistente netbeans
//       List<Cliente> clientes=new ArrayList<>();
//       ClienteREST cli=new ClienteREST(); 
//       clientes=cli.findAll_JSON(clientes.getClass());
//       System.out.println(clientes.size());
   }
}
