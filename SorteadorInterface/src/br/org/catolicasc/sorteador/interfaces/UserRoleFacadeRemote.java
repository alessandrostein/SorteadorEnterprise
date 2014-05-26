/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.catolicasc.sorteador.interfaces;

import br.org.catolicasc.sorteador.entity.UserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author alessandro
 */
@Remote
public interface UserRoleFacadeRemote {

    void create(UserRole userRole);

    void edit(UserRole userRole);

    void remove(UserRole userRole);

    UserRole find(Object id);

    List<UserRole> findAll();

    List<UserRole> findRange(int[] range);

    int count();
    
}
