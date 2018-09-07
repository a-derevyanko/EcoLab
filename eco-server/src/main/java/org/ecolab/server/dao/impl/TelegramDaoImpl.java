package org.ecolab.server.dao.impl;

import org.ecolab.server.dao.api.TelegramDao;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Lazy
public class TelegramDaoImpl implements TelegramDao {
  private final DSLContext dsl;

  public TelegramDaoImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public Set<Long> getChatIds() {
    return null;
  }

  @Override
  public void subscribe(Long chatId) {

  }

  @Override
  public void unSubscribe(Long chatId) {

  }
}
