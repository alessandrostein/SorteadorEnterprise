/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package java.sorteador.ejbs;

import javax.ejb.Remote;

/**
 *
 * @author alessandro
 */
@Remote
public interface SorteadorBeanRemote {
    
    int sortear();
    
}
