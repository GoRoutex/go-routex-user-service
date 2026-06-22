package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.common.PagedResult;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.repository.UserEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private static final int MAX_SEARCH_SIZE = 50;

    private final UserEntityRepository userEntityRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public Optional<User> findById(String id) {
        return userEntityRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public List<User> searchByKeyword(String keyword, int page, int size) {
        return userEntityRepository.searchByKeyword(
                        keyword == null ? "" : keyword.trim(),
                        PageRequest.of(Math.max(0, page), normalizeSearchSize(size), Sort.by(Sort.Order.asc("email")))
                ).stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public PagedResult<User> fetch(int pageNumber, int pageSize) {
        Page<User> page = userEntityRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .map(userPersistenceMapper::toDomain);
        return PagedResult.<User>builder()
                .items(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userEntityRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByPhoneNumberAndIdNot(String phoneNumber, String excludedId) {
        return userEntityRepository.existsByPhoneNumberAndIdNot(phoneNumber, excludedId);
    }

    @Override
    public User save(User user) {
        return userPersistenceMapper.toDomain(userEntityRepository.save(userPersistenceMapper.toEntity(user)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userEntityRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, String excludedId) {
        return userEntityRepository.existsByEmailAndIdNot(email, excludedId);
    }

    private int normalizeSearchSize(int size) {
        return Math.min(Math.max(size, 1), MAX_SEARCH_SIZE);
    }
}
