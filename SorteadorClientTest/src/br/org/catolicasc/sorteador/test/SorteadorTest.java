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

            addTitle("Removendo todos usuarios", true);
            removeAllUser();

            addTitle("Removendo todas regras", true);
            removeAllRole();

            addTitle("Removendo todos relacionamentos", true);
            removeAllUserRole();

            addTitle("Sorteando numero ...", true);
            Integer numberSorteado = sorteadorBean.sortear();
            addTitle("Numero sorteado: " + numberSorteado, true);

            addTitle("Novo usuario.", true);
            userFacade.create(newUser("Alessandro " + numberSorteado));

            listAllUser();

            addTitle("Nova regra", true);
            roleFacade.create(newRole("Regra " + numberSorteado));

            listAllRole();

            addTitle("Criando nova regra para os usuarios...", true);
            listRole = roleFacade.findAll();
            for (Role r : listRole) {
                listUser = userFacade.findAll();
                for (User u : listUser) {
                    userroleFacade.create(newUserRole(u, r));
                }
            }

            listAllUserRole();

            addTitle("Total de usuarios: " + userFacade.count(), true);
            addTitle("Total de regras: " + roleFacade.count(), false);
            addTitle("Total de relacionamentos: " + userroleFacade.count(), false);

            addTitle("Alterações: ", true);
            editAllUser("Fabio Dippold");
            editAllRole("Regra Tester");

            addTitle("Usuario:", false);
            listAllUser();
            addTitle("Regra:", false);
            listAllRole();

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

    public static User newUser(String name) {
        User u = new User();
        u.setName(name);
        return u;
    }

    public static Role newRole(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }

    public static UserRole newUserRole(User user, Role role) {
        UserRole ur = new UserRole();
        ur.setUserid(user.getId());
        ur.setRoleid(role.getId());
        return ur;
    }

    public static void removeAllUser() {
        List<User> listUser;
        listUser = userFacade.findAll();

        listUser.stream().forEach((u) -> {
            userFacade.remove(u);
        });
    }

    public static void removeAllRole() {
        List<Role> listRole;
        listRole = roleFacade.findAll();

        listRole.stream().forEach((u) -> {
            roleFacade.remove(u);
        });
    }

    public static void removeAllUserRole() {
        List<UserRole> listUserRole;
        listUserRole = userroleFacade.findAll();

        listUserRole.stream().forEach((u) -> {
            userroleFacade.remove(u);
        });
    }

    public static void listAllUser() {
        List<User> listUser;
        listUser = userFacade.findAll();
        listUser.forEach(s -> System.out.println("ID: " + s.getId() + " NAME: " + s.getName()));
    }

    public static void listAllRole() {
        List<Role> listRole;
        listRole = roleFacade.findAll();
        listRole.forEach(s -> System.out.println("ID: " + s.getId() + " NAME: " + s.getName()));
    }

    public static void listAllUserRole() {
        List<UserRole> listUserRole;
        listUserRole = userroleFacade.findAll();
        listUserRole.forEach(s -> System.out.println("ID: " + s.getId() + " IDUSER: " + s.getUserid() + " IDROLE: " + s.getRoleid()));
    }

    public static void editAllUser(String name) {
        List<User> listUser;
        listUser = userFacade.findAll();
        listUser.stream().forEach((u) -> {
            u.setName(name);
            userFacade.edit(u);
        });
    }

    public static void editAllRole(String name) {
        List<Role> listRole;
        listRole = roleFacade.findAll();
        listRole.stream().forEach((u) -> {
            u.setName(name);
            roleFacade.edit(u);
        });
    }
}
