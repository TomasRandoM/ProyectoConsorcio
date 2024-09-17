/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Mauro Sorbello
 */
@Entity
public class ExpensaInmueble implements Serializable {
    @Id
    private String id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
    private Estado estado;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    private Expensa expensa;
    private Inmueble inmueble;
    
    public enum Estado {}
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpensaInmueble)) {
            return false;
        }
        ExpensaInmueble other = (ExpensaInmueble) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ntm.consorcio.domain.entity.ExpensaInmueble[ id=" + id + " ]";
    }
    
}