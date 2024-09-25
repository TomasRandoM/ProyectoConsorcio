package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.ExpensaInmueble;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.ExpensaInmuebleServiceBean;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 * Controladr de ListExpensa
 * @version 1.0.0
 * @author Mauro Sorbello
 */
@ManagedBean
@ViewScoped
public class ControllerListExpensaInmueble {
    private @EJB ExpensaInmuebleServiceBean serviceBean;
    private Collection<ExpensaInmueble> expensaInmuebleList = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Agrega todos los objetos activos a la lista
     * @throws ErrorServiceException
     */
    public void listar() throws ErrorServiceException {
        try {
            expensaInmuebleList.clear();
            expensaInmuebleList.addAll(serviceBean.listarExpensaInmuebleActivo());

            RequestContext.getCurrentInstance().update("formPpal:expensaInmuebleTabla");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWhatsAppUrl(String periodo, String piso, String dpto, Double importe, String phoneInquilino, String phonePropietario,String nombreInquilino,String nombrePropietario){
        String phone;
        String nombre;
        if (phoneInquilino != null){
            phone = phoneInquilino;
            nombre = nombreInquilino;
        } else{
            phone = phonePropietario;
            nombre = nombrePropietario;
        }
        String message = String.format("Bueno dias %s\n, le informamos el monto %s\n de la expensa del periodo %s\n. Piso: %s\n dpto %s\n" ,
                                        nombre, importe, periodo, piso, dpto);                                   
        return "https://api.whatsapp.com/send?phone=" + phone+ "&text=" + message.replace("\n", "%0A");
    }
        /**
     * Getter de la lista
     * @return Collection
     */
    public Collection<ExpensaInmueble> getExpensaInmuebleList() {
        return this.expensaInmuebleList;
    }

        /**
     * Setter de lista
     * @param list Collection
     */
    public void setListExpensaInmueble(Collection<ExpensaInmueble> list) {
        this.expensaInmuebleList = list;
    }


    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editExpensaInmueble";

        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }

    /**
     * Método para manejar la opción de consulta
     * @param expensaInmueble ExpensaInmueble
     * @return String
     */
    public String consultar(ExpensaInmueble expensaInmueble) {
        try{
            guardarSession("CONSULTAR", expensaInmueble);
            return "editExpensaInmueble";

        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }

     /**
     * Método para manejar la opción de modificación
     * @param expensaInmueble ExpensaInmueble
     * @return String
     */
    public String modificar(ExpensaInmueble expensaInmueble) {
        try{
            guardarSession("MODIFICAR", expensaInmueble);
            return "editExpensaInmueble";

        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }

    /**
     * Método para manejar la opción de baja
     * @param expensaInmueble ExpensaInmueble
     */
    public void baja(ExpensaInmueble expensaInmueble) {
        try{
            serviceBean.eliminarExpensaInmueble(expensaInmueble.getId());
            listar();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

     /**
     * Guarda el caso de uso y el expensa en la sesión
     * @param casoDeUso String
     * @param expensaInmueble ExpensaInmueble
     */
    private void guardarSession(String casoDeUso, ExpensaInmueble expensaInmueble){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase());
        //Guarda el expense en el atributo EXPENSA
        session.setAttribute("EXPENSAINMUEBLE", expensaInmueble);
    }
}
