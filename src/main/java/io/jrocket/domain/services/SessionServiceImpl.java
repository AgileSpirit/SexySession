package io.jrocket.domain.services;

import io.jrocket.domain.entities.Session;
import io.jrocket.infra.repository.SessionRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Named
public class SessionServiceImpl implements SessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Inject
    SessionRepository sessionRepository;

    @Override
    public Session getSession(String token) {
        Session session = sessionRepository.findOne(token);
        if (session != null) {
            session.setTimestamp(new Date());
            session = sessionRepository.save(session);
        }
        return session;
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void cleanUpOldSessions() {
        LOGGER.info("Clean-up old sessions");

        DateTime limit = new DateTime();
        limit = limit.minusMinutes(Session.SESSION_DURATION);

        List<Session> oldSessions = sessionRepository.findByTimestampBefore(limit.toDate());
        long nbOldSessions = oldSessions.size();

        sessionRepository.delete(oldSessions);
        LOGGER.info(nbOldSessions + " sessions were deleted");

        long nbAliveSessions = sessionRepository.count();
        LOGGER.info(nbAliveSessions + " sessions stay in the DB");

    }
}
