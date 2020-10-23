/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.femass.gui;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import org.femass.dao.GrupoDao;
import org.femass.dao.UsuarioDao;
import org.femass.model.GrupoTrabalho;
import org.femass.model.Usuario;

/**
 *
 * @author Eduardo
 */
@Named(value = "guiGrupo")
@SessionScoped
public class GuiGrupo implements Serializable {

    /**
     * Creates a new instance of GuiGrupo
     */
    
    @EJB
    GrupoDao grupodao;
    
    @EJB
    UsuarioDao usuariodao;
    
    private GrupoTrabalho grupo;
    private Usuario lider;
    private long idLider;
    
    private List<GrupoTrabalho> grupos;
    private List<Usuario> usuarios;
    
    public GuiGrupo() {
    }
    
    public String inicializarLista(){
        grupos = grupodao.listar();
        usuarios = usuariodao.listar();
        return "LstGrupos";
    }
    
    public String cadastrar(){
        grupo = new GrupoTrabalho();
        return "CadGrupo";
    }
    
    public String gravar(){
        for(Usuario u:usuarios){
            if(u.getId().equals(idLider)){
                grupo.setLider(u);
            }
        }
        grupodao.gravar(grupo);
        return inicializarLista();
    }
    
    public List<GrupoTrabalho> getGrupos(){
        return grupos;
    }
    
    public GrupoTrabalho getGrupo(){
        return grupo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public long getIdLider() {
        return idLider;
    }

    public void setIdLider(long idLider) {
        this.idLider = idLider;
    }

    public Usuario getLider() {
        return lider;
    }

    public void setLider(Usuario lider) {
        this.lider = lider;
    }
    
    

    
}
