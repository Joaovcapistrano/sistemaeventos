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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private long idMembro;
    private GrupoTrabalho grupoSelecionado;
    private boolean alterando;

    private List<GrupoTrabalho> grupos;
    private List<Usuario> usuarios;

    public GuiGrupo() {
    }

    public String inicializarLista() {
        grupos = grupodao.listar();
        usuarios = usuariodao.listar();
        return "LstGrupos";
    }

    public String cadastrar() {
        grupo = new GrupoTrabalho();
        alterando = false;
        return "CadGrupo";
    }

    public String alterar(GrupoTrabalho _grupo) {
        this.grupo = _grupo;
        if (grupo.getLider() != null) {
            this.idLider = grupo.getLider().getId();
        }
        //this.idLider = grupo.getLider().getId();
        alterando = true;
        return "CadGrupo";
    }

    public String gravar() {
        for (Usuario u : usuarios) {
            if (u.getId().equals(idLider)) {
                grupo.setLider(u);
            }
        }
        if (alterando == false) {
            grupodao.gravar(grupo);
        } else {
            grupodao.alterar(grupo);
        }
        return inicializarLista();
    }

    public String excluir(GrupoTrabalho _grupo) {
        this.grupo = _grupo;
        for (Usuario u : grupo.getMembros()) {
            u.setGrupoTrabalho(null);
        }
        grupodao.deletar(grupo);
        return inicializarLista();
    }

    public List<GrupoTrabalho> getGrupos() {
        return grupos;
    }

    public GrupoTrabalho getGrupo() {
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

    public GrupoTrabalho getGrupoSelecionado() {
        return grupoSelecionado;
    }

    public void setGrupoSelecionado(GrupoTrabalho grupoSelecionado) {
        this.grupoSelecionado = grupoSelecionado;
    }

    public long getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(long idMembro) {
        this.idMembro = idMembro;
    }

    public String gerenciarMembro(GrupoTrabalho _grupo) {
        grupo = _grupo;
        return "AddMembros";
    }

    public String adicionarMembro() {
        for (Usuario u : usuarios) {
            if (u.getId().equals(idMembro)) {
                if (u.getGrupoTrabalho() == null) {
                    grupo.adicionarMembros(u);
                    u.setGrupoTrabalho(grupo);
                    usuariodao.alterar(u);
                    grupodao.alterar(grupo);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário já está em um grupo"));
                    return null;
                }
            }
        }
        return "AddMembros";
    }

    public String removerMembro(Usuario _membro) {
        grupo.removerMembros(_membro);
        _membro.setGrupoTrabalho(null);
        grupodao.alterar(grupo);
        usuariodao.alterar(_membro);
        return "AddMembros";
    }

}
