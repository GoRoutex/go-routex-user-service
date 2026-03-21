package vn.com.routex.hub.user.service.domain.user.port;

import vn.com.routex.hub.user.service.domain.user.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndEmail(String username, String email);

    User save(User user);
}
