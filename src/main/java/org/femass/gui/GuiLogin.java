/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.femass.gui;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.femass.dao.GrupoDao;
import org.femass.dao.TelefoneDao;
import org.femass.dao.UsuarioDao;
import org.femass.model.GrupoTrabalho;
import org.femass.model.NivelAcesso;
import org.femass.model.Telefone;
import org.femass.model.Usuario;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author João Victor Capistrano da Silva
 */
@Named(value = "guiLogin")
@SessionScoped
public class GuiLogin implements Serializable {

    /**
     * Creates a new instance of GuiLogin
     */
    @EJB
    UsuarioDao usuarioDao;
    
    private GuiUsuario guiUsuario;

    private List<Usuario> usuarios;

    private Boolean conectado = false;
    
    private String loginUsuario = null;
    private String senhaUsuario = null;
    private Usuario usuarioConectado = null;
    
    private Boolean verListaUsuarios = false;
    private Boolean verDetalhesUsuarios = false;
    private Boolean editarUsuarios = false;
    
    private Boolean verListaGrupos = false;
    private Boolean verDetalhesGrupos = false;
    private Boolean editarGrupos = false;
    
    private Boolean verListaEventos = false;
    private Boolean verDetalhesEventos = false;
    private Boolean editarEventos = false;

    public GuiLogin() {

    }

    public String inicializarLista() {
        usuarios = usuarioDao.listar();
        return "Login";
    }

    public Boolean getConectado() {
        return conectado;
    }

    public void setConectado(Boolean ativado) {
        this.conectado = ativado;
    }


    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public Usuario getUsuarioConectado() {
        return usuarioConectado;
    }

    public void setUsuarioConectado(Usuario usuarioConectado) {
        this.usuarioConectado = usuarioConectado;
    }

    public Boolean getVerListaUsuarios() {
        return verListaUsuarios;
    }

    public void setVerListaUsuarios(Boolean verListaUsuarios) {
        this.verListaUsuarios = verListaUsuarios;
    }

    public Boolean getVerDetalhesUsuarios() {
        return verDetalhesUsuarios;
    }

    public void setVerDetalhesUsuarios(Boolean verDetalhesUsuarios) {
        this.verDetalhesUsuarios = verDetalhesUsuarios;
    }

    public Boolean getEditarUsuarios() {
        return editarUsuarios;
    }

    public void setEditarUsuarios(Boolean editarUsuarios) {
        this.editarUsuarios = editarUsuarios;
    }
    

    public Boolean getVerListaGrupos() {
        return verListaGrupos;
    }

    public void setVerListaGrupos(Boolean verListaGrupos) {
        this.verListaGrupos = verListaGrupos;
    }

    public Boolean getVerDetalhesGrupos() {
        return verDetalhesGrupos;
    }

    public void setVerDetalhesGrupos(Boolean verDetalhesGrupos) {
        this.verDetalhesGrupos = verDetalhesGrupos;
    }

    public Boolean getEditarGrupos() {
        return editarGrupos;
    }

    public void setEditarGrupos(Boolean editarGrupos) {
        this.editarGrupos = editarGrupos;
    }

    public Boolean getVerListaEventos() {
        return verListaEventos;
    }

    public void setVerListaEventos(Boolean verListaEventos) {
        this.verListaEventos = verListaEventos;
    }

    public Boolean getVerDetalhesEventos() {
        return verDetalhesEventos;
    }

    public void setVerDetalhesEventos(Boolean verDetalhesEventos) {
        this.verDetalhesEventos = verDetalhesEventos;
    }

    public Boolean getEditarEventos() {
        return editarEventos;
    }

    public void setEditarEventos(Boolean editarEventos) {
        this.editarEventos = editarEventos;
    }
    
    
    public String conectar(){
        if(usuarios==null){
            usuarios = usuarioDao.listar();
        }
        for(Usuario u: usuarios)
        {
            if(u.getLogin().equals(loginUsuario))
            {
                if(u.getSenha().equals(senhaUsuario))
                {
                    usuarioConectado = u;
                    carregarPermissoes();
                    conectado = true;
                    //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário conectado!"));
                    return "index";
                }
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Credenciais inválidas!"));
        return null;
    }
    
    public String desconectar(){
        loginUsuario = null;
        senhaUsuario = null;
        usuarioConectado = null;
        conectado = false;
        carregarPermissoes();
        return "index";
    }
    
    
    public void acessoTotal(Boolean ac){  
        verListaUsuarios = ac;
        verDetalhesUsuarios = ac;
        editarUsuarios = ac;
    
        verListaGrupos = ac;
        verDetalhesGrupos = ac;
        editarGrupos = ac;
    
        verListaEventos = ac;
        verDetalhesEventos = ac;
        editarEventos = ac;    
    }
    
    public void carregarPermissoes(){
        if(usuarioConectado==null){
            acessoTotal(false);
        }
        else if(usuarioConectado.getNivelAcesso().equals(NivelAcesso.Administrador)){
            acessoTotal(true);
        }
        else{
            verListaUsuarios = true;
            verDetalhesUsuarios = false;
            editarUsuarios = false;

            verListaGrupos = true;
            verDetalhesGrupos = true;
            editarGrupos = false;

            verListaEventos = true;
            verDetalhesEventos = true;
            editarEventos = false;   
        }
    }
    
    public String alterarUsuario(){
        guiUsuario.setUser(usuarioConectado);
        return "CadUsuario";
    }
}
