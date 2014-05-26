/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.catolicasc.sorteador.test;

import br.org.catolicasc.sorteador.entity.Role;
import br.org.catolicasc.sorteador.entity.User;
import br.org.catolicasc.sorteador.entity.UserRole;
import br.org.catolicasc.sorteador.interfaces.RoleFacadeRemote;
import br.org.catolicasc.sorteador.interfaces.SorteadorBeanRemote;
import br.org.catolicasc.sorteador.interfaces.UserFacadeRemote;
import br.org.catolicasc.sorteador.interfaces.UserRoleFacadeRemote;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author alessandro
 */
public class SorteadorTest {

    /**
     * @param args the command line arguments
     */
    private static final String JNDI_NAME_SORTEADOR
            = "java:global/SorteadorEnterprise/SorteadorEnterprise-ejb/SorteadorBean";

    private static final String JNDI_NAME_USER
            = "java:global/SorteadorEnterprise/SorteadorEnterprise-ejb/UserFacade";

    private static final String JNDI_NAME_ROLE
            = "java:global/SorteadorEnterprise/SorteadorEnterprise-ejb/RoleFacade";

    private static final String JNDI_NAME_USER_ROLE
            = "java:global/SorteadorEnterprise/SorteadorEnterprise-ejb/UserRoleFacade";

    static UserFacadeRemote userFacade;
    static RoleFacadeRemote roleFacade;
    static UserRoleFacadeRemote userroleFacade;
    static SorteadorBeanRemote sorteadorBean;

    public static void main(String[] args) {

        InitialContext ctx;
        Properties props = new Properties();
        try {
            props.setProperty("java.naming.factory.initial",
                    "com.sun.enterprise.naming.SerialInitContextFactory");

            props.setProperty("java.naming.factory.url.pkgs",
                    "com.sun.enterprise.naming");

            props.setProperty("java.naming.factory.state",
                    "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

            // optional.  Defaults to localhost.  Only needed if web server is running 
            // on a different host than the appserver    
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");

            // optional.  Defaults to 3700.  Only needed if target orb port is not 3700.
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

            // variaveis
            List<User> listUser;
            List<Role> listRole;
            List<UserRole> listUserRole;

            // conexoes
            ctx = new InitialContext(props);
            sorteadorBean = (SorteadorBeanRemote) ctx.lookup(JNDI_NAME_SORTEADOR);
            userFacade = (UserFacadeRemote) ctx.lookup(JNDI_NAME_USER);
            roleFacade = (RoleFacadeRemote) ctx.lookup(JNDI_NAME_ROLE);
            userroleFacade = (UserRoleFacadeRemote) ctx.lookup(JNDI_NAME_USER_ROLE);

            addTitle("Removendo todos usuarios ....", true);
            listUser = userFacade.findAll();
            listUser.forEach(s -> userFacade.remove(s));

            addTitle("Removendo todas regras ....", true);
            listRole = roleFacade.findAll();
            listRole.forEach(s -> roleFacade.remove(s));

            addTitle("Removendo todas regras dos usuarios....", true);
            listUserRole = userroleFacade.findAll();
            listUserRole.forEach(s -> userroleFacade.remove(s));

            addTitle("Sorteando numero ...", true);
            Integer numberSorteado = sorteadorBean.sortear();
            addTitle("Numero sorteado: " + numberSorteado, true);

            addTitle("Criando novo usuario...", true);
            User user = new User();
            user.setName("Alessandro " + sorteadorBean.sortear());
            userFacade.create(user);

            addTitle("Carregando usuarios", true);
            listUser = userFacade.findAll();
            listUser.forEach(s -> System.out.println(s.getId() + " - " + s.getName()));

            addTitle("Criando nova regra...", true);
            Role role = new Role();
            role.setName("Regra " + numberSorteado);
            roleFacade.create(role);

            addTitle("Carregando regras...", true);
            listRole = roleFacade.findAll();
            listRole.forEach(s -> System.out.println(s.getId() + " - " + s.getName()));

            addTitle("Criando nova regra para os usuarios...", true);
            listRole = roleFacade.findAll();
            for (Role r : listRole) {
                listUser = userFacade.findAll();
                for (User u : listUser) {
                    UserRole userrole = new UserRole();
                    userrole.setUserid(u.getId());
                    userrole.setRoleid(r.getId());
                    userroleFacade.create(userrole);
                }
            }
            
            addTitle("Carregando regras dos usuarios...", true);
            listUserRole = userroleFacade.findAll();
            listUserRole.forEach(s -> System.out.println("ID: " + s.getId() + " USERID: " + s.getUserid() + " ROLEID: " + s.getRoleid()));

            addTitle("Total de usuarios: " + userFacade.count(), true);
            addTitle("Total de regras: " + roleFacade.count(), false);
            addTitle("Total de regras para usuarios: " + userroleFacade.count(), false);

            addTitle("Alterando usuario  " + user.getName(), true);
            listUser = userFacade.findAll();
            for (User u : listUser) {
                u.setName("Este usuario foi alterado >> " + u.getName());
                userFacade.edit(u);
            }

            addTitle("Listando usuario alterado...", true);
            listUser = userFacade.findAll();
            listUser.forEach(s -> System.out.println(s.getId() + " - " + s.getName()));

            addTitle("Alterando regra  " + role.getName(), true);
            listRole = roleFacade.findAll();
            for (Role r : listRole) {
                r.setName("Esta regra foi alterado >> " + r.getName());
                roleFacade.edit(r);
            }

            addTitle("Listando regras alterada...", true);
            listRole = roleFacade.findAll();
            listRole.forEach(s -> System.out.println(s.getId() + " - " + s.getName()));

        } catch (NamingException ex) {
            Logger.getLogger(SorteadorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void addTitle(String title, boolean pularLinha) {
        if (pularLinha) {
            System.out.println("");
        }
        System.out.println(title);
    }
}
