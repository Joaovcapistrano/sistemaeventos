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
import org.femass.model.Usuario;


@Stateless
public class UsuarioDao {
    
    @PersistenceContext
    EntityManager em;
    
    public void gravar(Usuario usuario) {
        em.persist(usuario);
    }
    
    public void alterar(Usuario usuario) {
        em.merge(usuario);
    }
    
    public void deletar(Usuario usuario){
        em.remove(em.merge(usuario));
    }
    
    public List<Usuario> listar()
    {
        Query q = em.createQuery("select u from Usuario u order by u.nomeCompleto");
        return q.getResultList();
    }

    public Usuario buscarID(Integer id)
    {
        Query q = em.createQuery("select u from Usuario u where u.id = :i");
        q.setParameter("i", id);
        return (Usuario) q.getSingleResult();
    }
    
    public void deletarParente(Usuario u1, Usuario u2)
    {
        Query q = em.createQuery("delete from parente where usuario_id = :i AND parentes_id = :j");
        q.setParameter("i", u1.getId());
        q.setParameter("j", u2.getId());
        q.executeUpdate();
        Query j = em.createQuery("delete from parente where usuario_id = :i AND parentes_id = :j");
        j.setParameter("j", u1.getId());
        j.setParameter("i", u2.getId());
        j.executeUpdate();
    }
    
}
