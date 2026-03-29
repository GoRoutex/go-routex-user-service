package vn.com.routex.hub.user.service.domain.user.port;

import vn.com.routex.hub.user.service.domain.user.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    User save(User user);
}
