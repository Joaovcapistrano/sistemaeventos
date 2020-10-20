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
import org.femass.model.Telefone;


@Stateless
public class TelefoneDao {
    
    @PersistenceContext
    EntityManager em;
    
    public void gravar(Telefone telefone) {
        em.persist(telefone);
    }
    
    public void alterar(Telefone telefone) {
        em.merge(telefone);
    }
    
    public void deletar(Telefone telefone){
        em.remove(telefone);
    }
    
    
}
