package cn.edu.nju.tagmakers.countsnju.security;

import cn.edu.nju.tagmakers.countsnju.data.dao.InitiatorDAO;
import cn.edu.nju.tagmakers.countsnju.data.dao.SecurityUserDAO;
import cn.edu.nju.tagmakers.countsnju.data.dao.WorkerDAO;
import cn.edu.nju.tagmakers.countsnju.entity.user.Initiator;
import cn.edu.nju.tagmakers.countsnju.entity.user.User;
import cn.edu.nju.tagmakers.countsnju.entity.user.Worker;
import cn.edu.nju.tagmakers.countsnju.entity.vo.PasswordVO;
import cn.edu.nju.tagmakers.countsnju.entity.vo.RegisterResponse;
import cn.edu.nju.tagmakers.countsnju.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import util.URLUtil;

/**
 * Description:
 * SecurityUserController
 *
 * @author WYM
 * Created on 04/07/2018
 */
@Component
@RestController
public class SecurityUserController implements UserDetailsService {

    private final SecurityUserDAO securityUserDAO;

    private final InitiatorDAO initiatorDAO;

    private final WorkerDAO workerDAO;

    @Autowired
    public SecurityUserController(SecurityUserDAO securityUserDAO, InitiatorDAO initiatorDAO, WorkerDAO workerDAO) {
        this.securityUserDAO = securityUserDAO;
        this.initiatorDAO = initiatorDAO;
        this.workerDAO = workerDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserDAO.findByID(username);

        if (securityUser == null) {
            throw new UsernameNotFoundException(username);
        }
//        String password=securityUser.getPassword().replaceAll("\\{noop}","");
//        securityUser.setSecurityPassword(password);
        return securityUser;
    }

    @RequestMapping(value = "/user/new_user", method = RequestMethod.POST)
    public RegisterResponse signUp(@RequestBody User user) {
        user.setPassword("{noop}" + user.getPassword());
        user.setAvatar(URLUtil.processURL(user.getAvatar()));
        SecurityUser securityUser = new SecurityUser(user);
        securityUserDAO.add(securityUser);
        switch (user.getRole()) {
            case INITIATOR:
                initiatorDAO.add(new Initiator(user));
                break;
            case WORKER:
                workerDAO.add(new Worker(user));
                break;
            case ADMIN:
                throw new InvalidInputException("不可以注册管理员");
        }

        return new RegisterResponse("ok");

    }

    @RequestMapping("/user/password/{user_name}")
    public boolean changePassword(@PathVariable(value = "user_name") String username, @RequestBody PasswordVO passwordVO) {
        return securityUserDAO.updatePassword(username, passwordVO.getOriPassword(), passwordVO.getNewPassword());
    }


}
