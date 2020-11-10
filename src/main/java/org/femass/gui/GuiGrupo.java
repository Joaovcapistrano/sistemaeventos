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
import java.util.Collections;
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
        /*for (Usuario u : usuarios) {
            if (u.getId().equals(idLider)) {
                grupo.setLider(u);
            }
        }*/
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
        this.grupo = _grupo;
        if (grupo.getLider() != null) {
            this.idLider = grupo.getLider().getId();
        }
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
        if (grupo.getLider() != null) {
            if (_membro.getId().equals(grupo.getLider().getId())) {
                grupo.setLider(null);
            }
        }
        grupodao.alterar(grupo);
        usuariodao.alterar(_membro);
        return "AddMembros";
    }

    public String removerTodosMembros() {
        for (Usuario m : grupo.getMembros()) {
            if (grupo.getLider() != null) {
                if (m.getId().equals(grupo.getLider().getId())) {
                    grupo.setLider(null);
                }
            }
            m.setGrupoTrabalho(null);
            usuariodao.alterar(m);
        }

        grupo.getMembros().clear();
        grupodao.alterar(grupo);
        return "AddMembros";
    }

    public String selecionarLider() {
        for (Usuario u : usuarios) {
            if (u.getId().equals(idLider)) {
                grupo.setLider(u);
            }
        }
        grupodao.alterar(grupo);
        return "AddMembros";
    }

    public String sortearGrupo() {
        int numgrupos = 0;
        int numusuarios = 0;
        int i = 0;
        grupos = grupodao.listar();
        usuarios = usuariodao.listar();

        for (GrupoTrabalho g : grupos) {
            numgrupos++;
        }
        for (Usuario u : usuarios) {
            numusuarios++;
        }

        //Define o numero médio de membros por grupo
        int nummembros = numusuarios / numgrupos;

        //Embaralha a lista de usuarios
        Collections.shuffle(usuarios);

        //Verifica se grupo já tem membros
        for (Usuario u : grupo.getMembros()) {
            i++;
        }

        //Faz o sorteio das vagas disponíveis
        for (Usuario u : usuarios) {
            if (u.getGrupoTrabalho() == null && i < nummembros) {
                grupo.adicionarMembros(u);
                u.setGrupoTrabalho(grupo);
                i++;
            }
        }
        
        grupodao.alterar(grupo);
        return "AddMembros";
    }

    public String sortearTodosGrupos() {
        int numgrupos = 0;
        int numusuarios = 0;
        int i = 0;
        List<Usuario> usuarios2 = usuariodao.listar();
        
        if(grupos.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não há grupos"));
            return null;
        }
        else{
            
            for (GrupoTrabalho g : grupos) {
                numgrupos++;
            }
            for (Usuario u : usuarios2) {
                numusuarios++;
            }

            int nummembros = numusuarios / numgrupos;

            //Define o número de usuários que sobraram
            float sobra = numusuarios % numgrupos;

            Collections.shuffle(usuarios2);

            for (GrupoTrabalho g : grupos) {
                for (Usuario u : g.getMembros()) {
                    i++;
                }
                for (Usuario u : usuarios2) {
                    if (u.getGrupoTrabalho() == null && i < nummembros) {
                        g.adicionarMembros(u);
                        u.setGrupoTrabalho(g);
                        i++;
                    }
                }
                grupodao.alterar(g);
                i = 0;
            }

            //Verifica se sobrou algum usuário e aumenta o número de vagas do grupo 
            if (sobra != 0) {
                i = 0;
                int nummembros2 = (numusuarios / numgrupos) + 1;
                for (GrupoTrabalho g : grupos) {
                    for (Usuario u : g.getMembros()) {
                        i++;
                    }
                    for (Usuario u : usuarios2) {
                        if (u.getGrupoTrabalho() == null && i < nummembros2) {
                            g.adicionarMembros(u);
                            u.setGrupoTrabalho(g);
                            i++;
                        }
                    }
                    grupodao.alterar(g);
                    i = 0;
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sorteio realizado"));
        }
        return "LstGrupos";
    }

}
