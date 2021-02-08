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
@Named(value = "guiUsuario")
@SessionScoped
public class GuiUsuario implements Serializable {

    /**
     * Creates a new instance of GuiUsuario
     */
    @EJB
    UsuarioDao usuarioDao;

    @EJB
    GrupoDao grupodao;

    @EJB
    TelefoneDao telefoneDao;

    private Usuario user;

    private List<Usuario> usuarios;

    private Telefone telefone = new Telefone();

    private Telefone telefoneSelecionado = new Telefone();

    private Boolean ativo = true;

    private Integer parente;

    private Usuario parenteSelecionado = new Usuario();

    private UploadedFile arquivoFoto;
    
    public GuiUsuario() {

    }

    public String inicializarLista() {
        usuarios = usuarioDao.listar();
        return "LstUsuario";
    }

    public String cadastrar() {
        user = new Usuario();
        telefone = new Telefone();

        return "CadUsuario";
    }

    public String gravar() {
        if (user.getId() == null) {
            usuarios = usuarioDao.listar();
            for (Usuario usuario : usuarios) {
                if (usuario.getLogin().equals(user.getLogin())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login já existente"));
                    return null;
                }
            }
            usuarioDao.gravar(user);
        } else {
            usuarioDao.alterar(user);
        }

        for (Usuario usuario : user.getParentes()) {
            usuarioDao.alterar(usuario);
        }

        return inicializarLista();
    }

    public String excluir() {

        List<Usuario> tempList = new ArrayList<>();
        tempList.addAll(user.getParentes());

        for (Usuario usuario : tempList) {
            user.removerParente(usuario);
            usuarioDao.alterar(usuario);
        }

        if (user.getGrupoTrabalho() == null) {
            usuarioDao.deletar(user);
        } else {
            GrupoTrabalho g = user.getGrupoTrabalho();
            if (g.getLider() == user) {
                g.setLider(null);
            }
            g.removerMembros(user);
            grupodao.alterar(g);
            usuarioDao.deletar(user);
        }
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

    public void novoTelefone() {
        telefone = new Telefone();
        telefone.setUsuario(user);
    }

    public void novoParente() {
        parente = null;

    }

    public Telefone getTelefoneSelecionado() {
        return telefoneSelecionado;
    }

    public void setTelefoneSelecionado(Telefone telefoneSelecionado) {
        this.telefoneSelecionado = telefoneSelecionado;
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

    public void removerTelefones() {
        user.removerTelefones(telefoneSelecionado);

    }

    public List<Usuario> listarParentes() {
        List<Usuario> parentes = new ArrayList<>();
        for (Usuario us : usuarioDao.listar()) {
            if (!us.equals(user) && !us.getParentes().contains(user)) {
                parentes.add(us);
            }
        }
        return parentes;
    }

    public void removerParente() {
        user.removerParente(parenteSelecionado);
        //parenteSelecionado.removerParente(user);
        usuarioDao.alterar(parenteSelecionado);
    }

    public void setAtivo(Boolean ativado) {
        this.ativo = ativado;
    }

    public Integer getParente() {
        return parente;
    }

    public void setParente(Integer parente) {
        this.parente = parente;
    }

    public void adicionarParente(Integer usuario) {
        Usuario par = usuarioDao.buscarID(usuario);
        if (!user.getParentes().contains(par)) {
            user.adicionarParente(par);
        }
    }

    public Usuario getParenteSelecionado() {
        return parenteSelecionado;
    }

    public void setParenteSelecionado(Usuario parenteSelecionado) {
        this.parenteSelecionado = parenteSelecionado;
    }

    public UploadedFile getArquivoFoto() {
        return arquivoFoto;
    }

    public void setArquivoFoto(UploadedFile arquivoFoto) {
        this.arquivoFoto = arquivoFoto;
    }

    public void ativarDesativar() {
        if (ativo == true) {
            ativo = false;
        } else {
            ativo = true;
        }
    }
}
