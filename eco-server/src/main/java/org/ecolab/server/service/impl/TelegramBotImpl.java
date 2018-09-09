package org.ecolab.server.service.impl;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.time.FastDateFormat;
import org.ecolab.server.service.api.TelegramBot;
import org.ecolab.server.service.api.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * К сожалению не работает из-за отсутствия прокси
 */
@ControllerAdvice
@ConditionalOnProperty(value = "telegram.bot.enabled", havingValue = "true")
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {
  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotImpl.class);
  private static final TelegramBotsApi BOTS_API = new TelegramBotsApi();
  private final TelegramService service;
  private final String name;
  private final String token;
  private static String PROXY_HOST = "88.255.185.216" /* proxy host */;
  private static Integer PROXY_PORT = 9050/* proxy port */;

  public TelegramBotImpl(TelegramService service, @Value("telegram.bot.name") String name, @Value("telegram.bot.token") String token) {
    super(defaultBotOptions());
    this.service = service;
    this.name = name;
    this.token = token;
  }

  private static DefaultBotOptions defaultBotOptions() {
    DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
    botOptions.setProxyHost(PROXY_HOST);
    botOptions.setProxyPort(PROXY_PORT);
    botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
    return botOptions;
  }

  @PostConstruct
  public void init() throws TelegramApiRequestException {
    ApiContextInitializer.init();
    BOTS_API.registerBot(this);
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (service.getChatIds().contains(update.getMessage().getChatId())) {
      if (update.getMessage().getLeftChatMember() != null) {
        service.unSubscribe(update.getMessage().getChatId());
      }
    } else {
      service.subscribe(update.getMessage().getChatId());
    }
  }

  @Override
  public String getBotUsername() {
    return name;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @ExceptionHandler(Exception.class)
  public void notifyAboutException(Exception ex) {
    SendDocument document = new SendDocument();
    document.setDocument(ex.getLocalizedMessage() + "_"
                    + FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss").format(new Date()) + ".txt",
            new ByteArrayInputStream(Throwables.getStackTraceAsString(ex).getBytes(StandardCharsets.UTF_8)));
    try {
      execute(document);
    } catch (TelegramApiException e) {
      System.err.println(String.format("Send telegram failed with exception {%s}", e.getMessage()));
    }
  }

 /* public synchronized void sendMsg(String chatId, String s) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(s);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      log.log(Level.SEVERE, "Exception: ", e.toString());
    }
  }*/
}
