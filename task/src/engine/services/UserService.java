package engine.services;

import engine.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public interface UserService {
    UserDetails loadUserByEmail(String email);
    User createUser(User user);
    User findByEmail(String email);
    List<User> findAllUsers();

}
