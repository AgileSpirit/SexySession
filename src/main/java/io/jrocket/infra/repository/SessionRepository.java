package io.jrocket.infra.repository;

import io.jrocket.domain.entities.Session;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface SessionRepository extends PagingAndSortingRepository<Session, String> {

    Session findByUserId(Long userId);

    List<Session> findByTimestampBefore(Date date);
}
