package io.jrocket.domain.entities;

import io.jrocket.infra.util.ApplicationException;
import io.jrocket.infra.util.JsonUtils;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static io.jrocket.infra.util.JsonUtils.JsonProperty;

@Entity
@Table(name = "T_SESSION")
public class Session implements Persistable<String> {

    /**
     * The maximum time-to-live of a Session (in minutes).
     */
    public static final int SESSION_DURATION = 30;

    /**
     * The delay (in minutes) from which the session timestamp is updated ; before this limit, if the session is
     * required, its timestamp is no refreshed, in order to minimize the writing DB requests.
     *
     * Notice : <code>SESSION_UPDATE_DELAY</code> must be less than <code>SESSION_DURATION</code>
     */
    public static final int SESSION_UPDATE_DELAY = 10;

    @Id
    private String token;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(nullable = true)
    private String data;

    public static Session newSession(String token, Long userId) {
        Session session = new Session();
        session.token = token;
        session.userId = userId;
        session.timestamp = new Date();
        return session;
    }

    /**
     * Generate a random session token formatted as a JSESSIONID
     *
     * @return The session token
     */
    public static String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void set(String key, String value) throws ApplicationException {
        JsonProperty jsonProperty = new JsonProperty(key, value);
        this.data = JsonUtils.set(this.data, jsonProperty);
    }

    @Override
    public String getId() {
        return token;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }

}
