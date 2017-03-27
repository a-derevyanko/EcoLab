package org.ekolab.server.dao.api;


import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
public interface TokenRepositoryDao {
    void insertToken(String userName, String series, String token, Date lastUsed);
}
