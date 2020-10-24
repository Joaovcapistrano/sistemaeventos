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
import org.femass.dao.TelefoneDao;
import org.femass.dao.UsuarioDao;
import org.femass.model.NivelAcesso;
import org.femass.model.Telefone;
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
    
    @EJB
    TelefoneDao telefoneDao;
    
    private Usuario user;
        
    private List<Usuario> usuarios;
    
    private Telefone telefone = new Telefone();
    
    private Boolean ativo=true;
        
    public GuiUsuario() {
  
    }
    
    public String inicializarLista(){
        usuarios = usuarioDao.listar();
        return "LstUsuario";
    }
    
    public String cadastrar(){
        user = new Usuario();
        telefone = new Telefone();
        return "CadUsuario";
    }
    
    public String gravar(){
        if(user.getId()==null)
        {
            usuarioDao.gravar(user);
        }
        else
        {
            usuarioDao.alterar(user);
        }
//        for(Telefone tel: user.getTelefones())
//        {
//            telefoneDao.gravar(tel);
//        }  
        return inicializarLista();
    }
    
    public String excluir(){
        usuarioDao.deletar(user);
        return inicializarLista();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }    
    
    public void novoTelefone(){
        telefone = new Telefone();
        telefone.setUsuario(user);
    }
    
    public Telefone getTelefone() {
        return telefone;
    }
    
    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public NivelAcesso[] getTiposDeUsuario() {
        return NivelAcesso.values();
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativado) {
        this.ativo = ativado;
    }
    
    public void ativarDesativar()
    {
        if(ativo==true)
        {
            ativo=false;
        }
        else
        {
            ativo=true;
        }
    }
}
