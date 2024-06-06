package engine.services;

import engine.adapters.UserAdapter;
import engine.entities.User;
import engine.exceptions.UserAlreadyExistException;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new UserAdapter(user);
    }

    @Override
    public User createUser(User user) {
        User findedUser = userRepository.findByEmail(user.getEmail());
        if (findedUser == null) {
            System.out.println("User Saved");
            return userRepository.save(user);
        } else {
            System.out.println("User Not Saved");
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
