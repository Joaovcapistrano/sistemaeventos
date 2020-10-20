/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.femass.gui;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import org.femass.dao.UsuarioDao;
import org.femass.model.Usuario;

/**
 *
 * @author João Victor Capistrano da Silva
 */
@Named(value = "guiUsuario")
@SessionScoped
public class GuiUsuario implements Serializable {

    /**
     * Creates a new instance of GuiUsuario
     */
    
    @EJB
    UsuarioDao usuarioDao;
    
    private List<Usuario> usuarios;
    
    public GuiUsuario() {
  
    }
    
    public String inicializarLista(){
        usuarios = usuarioDao.listar();
        return "LstUsuario";
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    
}
