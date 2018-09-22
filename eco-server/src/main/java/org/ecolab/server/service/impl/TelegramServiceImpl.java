package org.ecolab.server.service.impl;

import org.ecolab.server.dao.api.TelegramDao;
import org.ecolab.server.service.api.TelegramService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Lazy
public class TelegramServiceImpl implements TelegramService {
  private final TelegramDao dao;

  public TelegramServiceImpl(TelegramDao dao) {
    this.dao = dao;
  }

  @Override
  @Cacheable("TELEGRAM_CHATS")
  public Set<Long> getChatIds() {
    return dao.getChatIds();
  }

  @Override
  @Transactional
  public void subscribe(Long chatId) {
    dao.subscribe(chatId);
  }

  @Override
  @Transactional
  public void unSubscribe(Long chatId) {
    dao.unSubscribe(chatId);
  }
}
