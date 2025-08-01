package andream.eventManager.services;

import andream.eventManager.entities.User;
import andream.eventManager.exceptions.UnAuthorizedException;
import andream.eventManager.payloads.LoginDTO;
import andream.eventManager.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;


    public String checkAndCreateToken(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            String accessToken = jwtTools.createToken(found);
            return accessToken;
        } else {
            throw new UnAuthorizedException("invalid credentials!");
        }
    }
}
