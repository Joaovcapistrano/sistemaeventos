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
import org.femass.dao.EventoDao;
import org.femass.model.Evento;

/**
 *
 * @author João Victor Capistrano da Silva
 */
@Named(value = "guiEvento")
@SessionScoped
public class GuiEvento implements Serializable {

    /**
     * Creates a new instance of GuiEvento
     */
    
    @EJB
    EventoDao eventoDao;
    
    private List<Evento> eventos;
    
    private Evento evento;
    
    public GuiEvento() {
    }
    
    public String inicializarLista(){
        eventos = eventoDao.listar();
        evento = new Evento();
        return "LstEvento";
    }
    
    public String gravar(){
        if(evento.getId()==null){
            eventoDao.gravar(evento);
        }
        else{
            eventoDao.alterar(evento);
        }
        return inicializarLista();
    }
    
    public String cadastrar(){
        evento = new Evento();
        return "CadEvento";
    }
    
    public String excluir(){
        eventoDao.deletar(evento);
        return inicializarLista();
    }

    public EventoDao getEventoDao() {
        return eventoDao;
    }

    public void setEventoDao(EventoDao eventoDao) {
        this.eventoDao = eventoDao;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    public void onload(){
        eventos = eventoDao.listar();
    }
    
}
