/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.logic.entity;
import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOInmuebleBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Martinotebook
 */
public class InmuebleServiceBean {
    
    @EJB
    private DAOInmuebleBean dao;
    
    /**
     * Crea un objeto Inmueble.
     * @param piso String con el número de piso.
     * @param dpto String con el número de departamento.
     * @param estadoInmueble Estado del inmueble.
     * @throws ErrorServiceException
     */
    public void crearInmueble(String piso, String dpto, Inmueble.EstadoInmueble estadoInmueble) throws ErrorServiceException {
        try {
            if (piso == null || piso.isEmpty() || dpto == null || dpto.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el piso y el departamento.");
            }

            // Verificar si ya existe un inmueble con el mismo piso y departamento.
            try {
                dao.buscarInmueblePorPisoYDpto(piso, dpto);
                throw new ErrorServiceException("Ya existe un inmueble con el mismo piso y departamento.");
            } catch (NoResultDAOException ex) {
                // No existe, lo creamos.
            }

            Inmueble inmueble = new Inmueble();
            inmueble.setId(UUID.randomUUID().toString());
            inmueble.setPiso(piso);
            inmueble.setDpto(dpto);
            inmueble.setEstadoInmueble(estadoInmueble);
            inmueble.setEliminado(false);

            dao.guardarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Modifica un Inmueble existente.
     * @param idInmueble ID del inmueble.
     * @param piso Nuevo número de piso.
     * @param dpto Nuevo número de departamento.
     * @param estadoInmueble Nuevo estado del inmueble.
     * @throws ErrorServiceException
     */
    public void modificarInmueble(String idInmueble, String piso, String dpto, Inmueble.EstadoInmueble estadoInmueble) throws ErrorServiceException {
        try {
            Inmueble inmueble = buscarInmueble(idInmueble);

            if (piso == null || piso.isEmpty() || dpto == null || dpto.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el piso y el departamento.");
            }

            // Verificar si el nuevo piso y departamento ya está siendo usado por otro inmueble.
            try {
                Inmueble inmuebleExistente = dao.buscarInmueblePorPisoYDpto(piso, dpto);
                if (!inmuebleExistente.getId().equals(idInmueble)) {
                    throw new ErrorServiceException("Ya existe un inmueble con el mismo piso y departamento.");
                }
            } catch (NoResultDAOException ex) {
                // No existe conflicto, podemos modificar.
            }

            inmueble.setPiso(piso);
            inmueble.setDpto(dpto);
            inmueble.setEstadoInmueble(estadoInmueble);

            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Busca un inmueble por su ID.
     * @param id ID del inmueble.
     * @return Inmueble encontrado.
     * @throws ErrorServiceException
     */
    public Inmueble buscarInmueble(String id) throws ErrorServiceException {
        try {
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el ID del inmueble.");
            }

            Inmueble inmueble = dao.buscarInmueble(id);

            if (inmueble.getEliminado()) {
                throw new ErrorServiceException("El inmueble está eliminado.");
            }

            return inmueble;

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Elimina (lógicamente) un inmueble.
     * @param id ID del inmueble.
     * @throws ErrorServiceException
     */
    public void eliminarInmueble(String id) throws ErrorServiceException {
        try {
            Inmueble inmueble = buscarInmueble(id);
            inmueble.setEliminado(true);
            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Lista todos los inmuebles activos (no eliminados).
     * @return Colección de inmuebles activos.
     * @throws ErrorServiceException
     */
    public Collection<Inmueble> listarInmueblesActivos() throws ErrorServiceException {
        try {
            return dao.listarInmueblesActivos();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

}