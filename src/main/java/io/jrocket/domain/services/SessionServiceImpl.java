package io.jrocket.domain.services;

import io.jrocket.domain.entities.Session;
import io.jrocket.infra.repository.SessionRepository;
import io.jrocket.infra.util.Features;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class SessionServiceImpl implements SessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Inject
    SessionRepository sessionRepository;

    @Override
    public Session getSession(String token) {
        Session session = sessionRepository.findOne(token);
        if (Features.IS_ENABLED_SESSION_UPDATE_OPTIMIZATION) {
            if (session != null) {
                /*
                 * The session timestamp is updated only every 10mn,
                 * in order to reduce the writing requests.
                 */
                DateTime now = new DateTime();
                DateTime limitTime = now.minusMinutes(Session.SESSION_UPDATE_DELAY);
                DateTime sessionLastUpdateTime = new DateTime(session.getTimestamp());
                if (sessionLastUpdateTime.isBefore(limitTime)) {
                    session.setTimestamp(now.toDate());
                    session = sessionRepository.save(session);
                }
            }
        }
        return session;
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void cleanUpOldSessions() {
        if (Features.IS_ENABLED_OLD_SESSIONS_PURGE) {
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
}
