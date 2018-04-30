package cn.edu.nju.tagmakers.countsnju.data.pycontroller;

import cn.edu.nju.tagmakers.countsnju.data.dao.SecurityUserDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.User;
import cn.edu.nju.tagmakers.countsnju.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/29/2018
 */

@RestController
public class PYSecurityController {
    final
    SecurityUserDAO securityUserDAO;

    @Autowired
    public PYSecurityController(SecurityUserDAO securityUserDAO) {
        this.securityUserDAO = securityUserDAO;
    }

    @RequestMapping(value = "/py/security_user", method = RequestMethod.POST)
    public void add(@RequestBody User securityUser) {
        if (securityUserDAO.findByID(securityUser.getPrimeKey()) == null) {
            securityUserDAO.add(new SecurityUser(securityUser));
        } else {
            securityUserDAO.delete(securityUser.getPrimeKey());
            securityUserDAO.add(new SecurityUser(securityUser));
        }
    }

}
