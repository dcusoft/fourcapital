/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "deuda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deuda.findAll", query = "SELECT d FROM Deuda d")
    , @NamedQuery(name = "Deuda.findByIddeuda", query = "SELECT d FROM Deuda d WHERE d.deudaPK.iddeuda = :iddeuda")
    , @NamedQuery(name = "Deuda.findByAgencia", query = "SELECT d FROM Deuda d WHERE d.agencia = :agencia")
    , @NamedQuery(name = "Deuda.findByMonto", query = "SELECT d FROM Deuda d WHERE d.monto = :monto")
    , @NamedQuery(name = "Deuda.findByEstado", query = "SELECT d FROM Deuda d WHERE d.estado = :estado")
    , @NamedQuery(name = "Deuda.findByDescargas", query = "SELECT d FROM Deuda d WHERE d.descargas = :descargas")
    , @NamedQuery(name = "Deuda.findByProducto", query = "SELECT d FROM Deuda d WHERE d.producto = :producto")
    , @NamedQuery(name = "Deuda.findByClienteIdcliente", query = "SELECT d FROM Deuda d WHERE d.deudaPK.clienteIdcliente = :clienteIdcliente")})
public class Deuda implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeudaPK deudaPK;
    @Size(max = 45)
    @Column(name = "agencia")
    private String agencia;
    @Size(max = 45)
    @Column(name = "monto")
    private String monto;
    @Size(max = 45)
    @Column(name = "estado")
    private String estado;
    @Size(max = 45)
    @Column(name = "descargas")
    private String descargas;
    @Size(max = 45)
    @Column(name = "producto")
    private String producto;
    @JoinColumn(name = "cliente_idcliente", referencedColumnName = "idcliente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public Deuda() {
    }

    public Deuda(DeudaPK deudaPK) {
        this.deudaPK = deudaPK;
    }

    public Deuda(int iddeuda, int clienteIdcliente) {
        this.deudaPK = new DeudaPK(iddeuda, clienteIdcliente);
    }

    public DeudaPK getDeudaPK() {
        return deudaPK;
    }

    public void setDeudaPK(DeudaPK deudaPK) {
        this.deudaPK = deudaPK;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescargas() {
        return descargas;
    }

    public void setDescargas(String descargas) {
        this.descargas = descargas;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }
    @JsonbTransient
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deudaPK != null ? deudaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deuda)) {
            return false;
        }
        Deuda other = (Deuda) object;
        if ((this.deudaPK == null && other.deudaPK != null) || (this.deudaPK != null && !this.deudaPK.equals(other.deudaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Deuda[ deudaPK=" + deudaPK + " ]";
    }
    
}
