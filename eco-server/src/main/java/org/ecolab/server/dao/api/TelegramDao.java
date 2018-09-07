package org.ecolab.server.dao.api;

import java.util.Set;

public interface TelegramDao {
  Set<Long> getChatIds();

  void subscribe(Long chatId);

  void unSubscribe(Long chatId);
}
