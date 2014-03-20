package io.jrocket.infra.repository;

import io.jrocket.domain.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository  extends PagingAndSortingRepository<User, Long> {

    User findByLogin(String login);

}
