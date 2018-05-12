/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.input.KeyCode.T;

/**
 *
 * @author Usuario
 */
public class ConvertirEntidadForDTO {

    public ConvertirEntidadForDTO() {
    }
    public static <T> T convert(T T){
        Method[] f;
        try {
            f = Class.forName(T.getClass().getName()).getMethods();
        
        for (Method f1 : f) {
            
            System.out.println(f1.getName());
        }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConvertirEntidadForDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ConvertirEntidadForDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return T;
    }
}
