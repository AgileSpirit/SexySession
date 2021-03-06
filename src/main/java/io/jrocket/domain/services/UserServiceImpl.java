package io.jrocket.domain.services;

import io.jrocket.domain.entities.Session;
import io.jrocket.domain.entities.User;
import io.jrocket.infra.repository.SessionRepository;
import io.jrocket.infra.repository.UserRepository;
import io.jrocket.infra.util.ApplicationException;
import io.jrocket.infra.util.Features;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    StringRedisTemplate redisTemplate;

    @Override
    public String signIn(String login, String password) throws ApplicationException {
        User user = userRepository.findByLogin(login);

        if (user == null || !user.getPassword().equals(password)) {
            throw new ApplicationException();
        }

        // Check if a current session is open, then retrieve it
        Session session = sessionRepository.findByUserId(user.getId());

        // The user has no opened session
        if (session == null) {

            // Generate a session token
            String token = Session.generateToken();

            // Instance a new session
            session = Session.newSession(token, user.getId());

            // Persist the session in DB
            session = sessionRepository.save(session);

            if (Features.IS_ENABLED_REDIS_STORING) {
                // Store session in Redis
                redisTemplate.opsForList().leftPush(token, login);
            }
        }

        return session.getToken();
    }

    @Override
    public void signOut(String token) throws ApplicationException {
        // Retrieve the session
        Session session = sessionRepository.findOne(token);

        if (session == null) {
            throw new ApplicationException();
        }

        // Remove the session
        sessionRepository.delete(session);
    }

}
