package org.ecolab.server.service.api;

import java.util.Set;

public interface TelegramService {
    Set<Long> getChatIds();

    void subscribe(Long chatId);

    void unSubscribe(Long chatId);
}
