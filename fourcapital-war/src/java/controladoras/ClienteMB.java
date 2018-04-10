/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladoras;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

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
    
}
