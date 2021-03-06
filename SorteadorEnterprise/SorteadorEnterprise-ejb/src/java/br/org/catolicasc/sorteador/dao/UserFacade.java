/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.catolicasc.sorteador.dao;

import br.org.catolicasc.sorteador.interfaces.UserFacadeRemote;
import br.org.catolicasc.sorteador.entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alessandro
 */
@Stateless
public class UserFacade extends AbstractFacade<User> implements UserFacadeRemote {
    @PersistenceContext(unitName = "SorteadorEnterprise-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
}
