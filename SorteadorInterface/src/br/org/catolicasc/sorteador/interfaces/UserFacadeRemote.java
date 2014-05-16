/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.catolicasc.sorteador.interfaces;

import br.org.catolicasc.sorteador.entity.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author alessandro
 */
@Remote
public interface UserFacadeRemote {

    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(Object id);

    List<User> findAll();

    List<User> findRange(int[] range);

    int count();
    
}
