package com.example.startup.bot_service;

import com.example.startup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RegisterBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final Map<Long, String> phoneBuffer = new ConcurrentHashMap<>();

    @Override
    public String getBotUsername() {
        return "@UstaBorOfficial_bot";
    }

    @Override
    public String getBotToken() {
        return "8139210855:AAGOCixU_IzdqnZXKyFr8I-Tb2HYtNa0bx8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (message.hasText()) {
                String text = message.getText();

                if (text.equals("/start")) {
                    sendPhoneRequest(chatId);
                    return;
                }

                if ((text.equals("👤 Mijoz") || text.equals("🛠 Usta")) && phoneBuffer.containsKey(chatId)) {
                    String phone = phoneBuffer.remove(chatId);
                    String role = text.equals("👤 Mijoz") ? "CUSTOMER" : "WORKER";

                    boolean exists = userService.checkUserExistsByPhone(phone);
                    SendMessage reply = new SendMessage();
                    reply.setChatId(chatId.toString());

                    if (exists) {
                        reply.setText("❗️Siz allaqachon ro'yxatdan o'tgansiz.");
                    } else {
                        String key = userService.createUserAndReturnKey(phone, role);
                        reply.setText("""
                                ✅ Siz muvaffaqiyatli ro'yxatdan o'tdingiz.
                                Roli: %s
                                Login kalitingiz: `%s`
                                """.formatted(role.equals("CUSTOMER") ? "Mijoz" : "Usta", key));
                        reply.setParseMode("Markdown");
                    }

                    executeSafely(reply);
                }
            }

            if (message.hasContact()) {
                String phone = message.getContact().getPhoneNumber();
                phoneBuffer.put(chatId, phone);
                sendRoleOptions(chatId);
            }
        }
    }

    private void sendPhoneRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Iltimos, telefon raqamingizni yuboring:");
        sendMessage.setReplyMarkup(createRequestPhoneButton());
        executeSafely(sendMessage);
    }

    private void sendRoleOptions(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Rolingizni tanlang:");
        message.setReplyMarkup(createRoleKeyboard());
        executeSafely(message);
    }

    private ReplyKeyboardMarkup createRequestPhoneButton() {
        KeyboardButton button = new KeyboardButton("📱 Telefon raqamni yuborish");
        button.setRequestContact(true);
        KeyboardRow row = new KeyboardRow();
        row.add(button);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(row));
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    private ReplyKeyboardMarkup createRoleKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add("👤 Mijoz");
        row.add("🛠 Usta");
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(row));
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    private void executeSafely(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
