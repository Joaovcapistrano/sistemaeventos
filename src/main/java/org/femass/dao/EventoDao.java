/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.femass.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.femass.model.Evento;


@Stateless
public class EventoDao {
    
    @PersistenceContext
    EntityManager em;
    
    public void gravar(Evento evento) {
        em.persist(evento);
    }
    
    public void alterar(Evento evento) {
        em.merge(evento);
    }
    
    public void deletar(Evento evento){
        em.remove(evento);
    }
    
    
}
