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
import org.femass.model.GrupoTrabalho;


@Stateless
public class GrupoDao {
    
    @PersistenceContext
    EntityManager em;
    
    public void gravar(GrupoTrabalho grupo) {
        em.persist(grupo);
    }
    
    public void alterar(GrupoTrabalho grupo) {
        em.merge(grupo);
    }
    
    public void deletar(GrupoTrabalho grupo){
        em.remove(grupo);
    }
    
    
}
