package io.jrocket.infra.util;

import com.google.common.collect.Lists;
import io.jrocket.domain.entities.Session;
import io.jrocket.domain.entities.User;
import io.jrocket.infra.repository.SessionRepository;
import io.jrocket.infra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Named
public class DataGenerator {

    @Inject
    UserRepository userRepository;

    @Inject
    SessionRepository sessionRepository;

    private static Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    /**
     * Generate and persist some users and simulated sessions.
     */
    public void populateData() {
        generateUsers();
        generateSessions();

    }

    private final static int MAX_USERS = 10000;

    /**
     * Generate and persist some users.
     */
    private void generateUsers() {
        logger.info("Generating users ...");
        List<User> users = Lists.newArrayList();

        users.add(newUser("admin", "admin"));
        users.add(newUser("test", "test"));

        // Add MAX_USERS (login = "userX", password = "toto")
        for (int i = 0 ; i < MAX_USERS ; i++) {
            users.add(newUser("user" + i, "toto"));
        }
        userRepository.save(users);

        long count = userRepository.count();
        logger.info("There is " + count + " users in the DB");
    }

    private final static int MAX_SESSIONS = 0;

    /**
     * Generate and persist MAX_SESSIONS sessions.
     */
    private void generateSessions() {
        logger.info("Generating sessions ...");
        List<Session> sessions = new ArrayList<>(MAX_SESSIONS);
        Random random = new Random();
        for (int i = 0; i < MAX_SESSIONS; i++) {
            String token = Session.generateToken();
            Session session = Session.newSession(token, random.nextLong());
            sessions.add(session);
        }
        sessionRepository.save(sessions);

        long count = sessionRepository.count();
        logger.info("There is " + count + " sessions in the DB");
    }

    /**
     * Generate a user
     *
     * @param login
     * @param password
     * @return
     */
    private User newUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }

}
