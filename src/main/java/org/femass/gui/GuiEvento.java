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
import org.femass.dao.EventoDao;
import org.femass.dao.UsuarioDao;
import org.femass.model.Evento;
import org.femass.model.Usuario;

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
    
    @EJB
    UsuarioDao usuarioDao;
    
    private List<Evento> eventos;
    
    private Integer ano = null;
    
    private Evento evento;
    
    public GuiEvento() {
    }
    
    public String inicializarLista(){
        eventos = eventoDao.listar();
        //evento = new Evento();
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
    
    public String cadastrarAniversarios(){
        if(ano!=null)
        {
        for(Usuario usuario: usuarioDao.listar()){
            
            //Verifica se o usuário já tem um aniversário do usuário cadastrado naquele ano, se houver, o evento antigo é deletado
            for(Evento usuarioAniv: eventoDao.listar()){
                if(usuarioAniv.getNome().equals("Aniversário de " + usuario.getNomeCompleto()) &&  usuarioAniv.getDataInicio().getYear()==ano)
                {
                    eventoDao.deletar(usuarioAniv);
                }
            }
            Evento ev = new Evento();
            ev.setNome("Aniversário de " + usuario.getNomeCompleto());
            ev.setDescricao("Aniversário de " + usuario.getNomeCompleto());
            
            
            ev.setDataInicio(usuario.getDataNascimento().withYear(ano));
            ev.setDataFim(usuario.getDataNascimento().withYear(ano));
                        
            //Verifica se a data de aniversário do usuário já passou no ano atual
            //Caso tenha passado, gera um evento de aniversário no ano seguinte
            //Caso não tenha passado, gera um evento de aniversário no ano atual
//            if(usuario.getDataNascimento().withYear(LocalDate.now().getYear()).isBefore(LocalDate.now()))
//            {
//                ev.setDataInicio(usuario.getDataNascimento().withYear(LocalDate.now().getYear()).plusYears(1));
//                ev.setDataFim(usuario.getDataNascimento().withYear(LocalDate.now().getYear()).plusYears(1));
//            }
//            else
//            {
//                ev.setDataInicio(usuario.getDataNascimento().withYear(LocalDate.now().getYear()));
//                ev.setDataFim(usuario.getDataNascimento().withYear(LocalDate.now().getYear()));
//            }
            
            eventoDao.gravar(ev);
        }
        }
        ano=null;
        return inicializarLista();
    }

    public EventoDao getEventoDao() {
        return eventoDao;
    }

    public void setEventoDao(EventoDao eventoDao) {
        this.eventoDao = eventoDao;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
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
