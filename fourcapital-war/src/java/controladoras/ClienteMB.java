/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladoras;

import bo.ClienteBO;
import bo.SolicitudBO;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionHTTP;
import dto.ClienteDTO;
import dto.DeudaDTO;
import dto.DeudaPKDTO;
import dto.SolicitudDTO;
import entities.Cliente;
import entities.Deuda;
import entities.DeudaPK;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Session;
import org.primefaces.PrimeFaces;
import conexion.EnvioCorreo;

/**
 *
 * @author Usuario
 */
@Named(value = "clienteMB")
@SessionScoped
public class ClienteMB implements Serializable {

    @EJB
    private ClienteBO clienteBO;

    @EJB
    private SolicitudBO solicitudBO;

    /**
     * Creates a new instance of ClienteMB
     */
    @Resource(name = "gmailTest")
    private Session s;
    private String dni, nombreBoton;
    private ClienteDTO cliente;
    private DeudaDTO deudaSelect;
    private boolean estadoBoton;
    private SolicitudDTO SolicitudProceso;
    private boolean botonSolicitud;
    private String botonSolicitudName;
    String servicio = "http://localhost:8080/fourcapitalservice/webresources/entidades.cliente";

    public ClienteMB() {
    }

    @PostConstruct
    public void init() {
        // SolicitudProceso = new SolicitudDTO();
    }

    public void listcliente(ActionEvent actionEvent) {

        //coneccion por medio de HTTP
        ConexionHTTP connet = new ConexionHTTP();
        List<ClienteDTO> clientes;
        Type type = new TypeToken<List<ClienteDTO>>() {
        }.getType();
        clientes = connet.conectarServicio(servicio, "", type);
        System.out.println(clientes.size());

        // coneccion por medio de cliente del asistente netbeans
//       List<Cliente> clientes=new ArrayList<>();
//       ClienteREST cli=new ClienteREST(); 
//       clientes=cli.findAll_JSON(clientes.getClass());
//       System.out.println(clientes.size());
    }

    public void getCliente(ActionEvent actionEvent) {

        //coneccion por medio de HTTP
        ConexionHTTP connet = new ConexionHTTP();
        Type type = new TypeToken<Cliente>() {
        }.getType();
        cliente = clienteForDTO(connet.conectarServicio(servicio, dni, type));

        try {
            if (cliente == null) {

                //FacesContext.getCurrentInstance().getExternalContext().redirect("faces/PrincipalJSF.xhtml");
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/noAdeudoJSF.xhtml");
                return;
            }
            SolicitudProceso = solicitudBO.solicitudForDNI(dni);
            if (SolicitudProceso != null) {
                if (SolicitudProceso.getCorreo().equals(cliente.getCorreo())
                        && SolicitudProceso.getDireccion().equals(cliente.getDireccion())
                        && SolicitudProceso.getNombre().equals(cliente.getNombre())
                        && SolicitudProceso.getTelefono().equals(cliente.getTelefono())) {
                    botonSolicitud = false;
                    botonSolicitudName = "Solicitar Cambio";
                    SolicitudProceso.setEstado("finalizado");
                    solicitudBO.editarSolicitud(SolicitudProceso);
                } else {
                    botonSolicitud = true;
                    botonSolicitudName = "Procesando...";
                }
            } else {

                botonSolicitud = false;
                botonSolicitudName = "Solicitar Cambio";
            }
            SolicitudProceso = new SolicitudDTO();
            FacesContext.getCurrentInstance().getExternalContext().redirect("faces/adeudoJSF.xhtml");

        } catch (IOException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargaModalSolicitud(ActionEvent actionEvent) {

        SolicitudProceso.setCorreo(cliente.getCorreo());
        SolicitudProceso.setDireccion(cliente.getDireccion());
        SolicitudProceso.setNombre(cliente.getNombre());
        SolicitudProceso.setTelefono(cliente.getTelefono());
        PrimeFaces.current().ajax().update("form:dlgSolicitud");
        PrimeFaces.current().executeScript("PF('dlg').show();");
    }

    public void enviarSolicitud(ActionEvent actionEvent) {
        SolicitudProceso.setClienteIdcliente(cliente);
        SolicitudProceso.setEstado("procesando");
        solicitudBO.nuevaSolicitud(SolicitudProceso);
        botonSolicitud = true;
        System.out.println("SE VA ENVIAR UN CORREO");
        EnvioCorreo ec = new EnvioCorreo(s);
        ec.setOrigen("jorgeevj@gmail.com");
        ec.setClave("evjegroj28");
        ec.setDestino("jorgeevj@gmail.com");
        ec.setAsunto("Solicitud de Cambio de datos");
        ec.setMensaje("<html>"
                + "<h1><font color='ff0000'>Solicitud de cambio de datos</font></h2>"
                + "<h2><font color='ffffff'>El cliente con DNI " + cliente.getIdcliente() + " solicita el cambio de los siguientes Datos</font></h2>"
                + "<p>" + "NOMBRE: " + SolicitudProceso.getNombre() + "</p>"
                + "<p>" + "DIRECCION: " + SolicitudProceso.getDireccion() + "</p>"
                + "<p>" + "CORREO: " + SolicitudProceso.getCorreo() + "</p>"
                + "<p>" + "TELEFONO: " + SolicitudProceso.getTelefono() + "</p>"
                + "</html>");
        ec.envio();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ENVIO", "Se envio correctamente la solicitud"));
        PrimeFaces.current().executeScript("PF('dlg').hide();");
        botonSolicitudName = "Procesando...";
        PrimeFaces.current().ajax().update("form:panel");
        PrimeFaces.current().ajax().update("form:growl");

    }

    public void descargarPDF() {
        //RequestContext.getCurrentInstance().execute("PF('dlg1').show();"); //deprecated
        System.out.println("Estames");
        if (deudaSelect.getEstadoCarta().equalsIgnoreCase("pendiente")) {
            PrimeFaces.current().executeScript("PF('dlg1').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlg2').show();");
        }

    }

    public void ActualizarCliente() {
        SolicitudDTO dto = solicitudBO.solicitudForDNI(dni);
        cliente.setCorreo(dto.getCorreo());
        cliente.setDireccion(dto.getDireccion());
        cliente.setNombre(dto.getNombre());
        cliente.setTelefono(dto.getTelefono());
        clienteBO.editar(cliente);

        dto.setEstado("finalizado");
        solicitudBO.editarSolicitud(dto);
        botonSolicitudName = "Procesando...";
        botonSolicitud = false;
        botonSolicitudName = "Solicitar Cambio";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Actualizado", "Se actualizo el cliente"));

        PrimeFaces.current().ajax().update("form:panel");
        PrimeFaces.current().ajax().update("form:growl");
    }

    private ClienteDTO clienteForDTO(Cliente entidad) {
        ClienteDTO dto = new ClienteDTO();
        dto.setCorreo(entidad.getCorreo());
        dto.setDeudaList(listDeudaForDTO(entidad.getDeudaList()));
        dto.setDireccion(entidad.getDireccion());
        dto.setIdcliente(entidad.getIdcliente());
        dto.setNombre(entidad.getNombre());
        dto.setTelefono(entidad.getTelefono());
        dto.setTipo(entidad.getTipo());
        return dto;
    }


    private List<DeudaDTO> listDeudaForDTO(List<Deuda> deudaList) {
        List<DeudaDTO> ldto = new ArrayList<>();
        for (Deuda d : deudaList) {
            ldto.add(deudaForDTO(d));
        }
        return ldto;
    }

    private DeudaDTO deudaForDTO(Deuda d) {
        DeudaDTO dto = new DeudaDTO();
        dto.setAgencia(d.getAgencia());
        dto.setDeudaPK(DeudaPKForDTO(d.getDeudaPK()));
        dto.setMonto(d.getMonto());
        dto.setEstadoCarta(d.getEstadoCarta());
        dto.setProducto(d.getProducto());
        return dto;
    }

    private DeudaPKDTO DeudaPKForDTO(DeudaPK deudaPK) {
        DeudaPKDTO dto = new DeudaPKDTO();
        dto.setClienteIdcliente(deudaPK.getClienteIdcliente());
        dto.setIddeuda(deudaPK.getIddeuda());
        return dto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEstadoBoton(boolean estadoBoton) {
        this.estadoBoton = estadoBoton;
    }

    public String getNombreBoton() {
        return nombreBoton;
    }

    public void setNombreBoton(String nombreBoton) {
        this.nombreBoton = nombreBoton;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public DeudaDTO getDeudaSelect() {
        return deudaSelect;
    }

    public void setDeudaSelect(DeudaDTO deudaSelect) {
        this.deudaSelect = deudaSelect;
    }

    public SolicitudDTO getSolicitudProceso() {
        return SolicitudProceso;
    }

    public void setSolicitudProceso(SolicitudDTO SolicitudProceso) {
        this.SolicitudProceso = SolicitudProceso;
    }

    public boolean isBotonSolicitud() {
        return botonSolicitud;
    }

    public void setBotonSolicitud(boolean botonSolicitud) {
        this.botonSolicitud = botonSolicitud;
    }

    public String getBotonSolicitudName() {
        return botonSolicitudName;
    }

    public void setBotonSolicitudName(String botonSolicitudName) {
        this.botonSolicitudName = botonSolicitudName;
    }

}
