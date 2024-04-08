package com.example.l4s45454;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

import java.io.FileWriter;
import java.io.IOException;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "MyQueue")
})
public class MessageReceiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String originalMessage = textMessage.getText();

                // Обработка сообщения и замена символов
                String maskedMessage = maskMessage(originalMessage);

                // Вывод информации о полученном и обработанном сообщении
                System.out.println("Получено сообщение: " + originalMessage);
                System.out.println("Обработанное сообщение: " + maskedMessage);

                // Запись сообщений в файл
                writeToFile(maskedMessage, originalMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String maskMessage(String message) {
        // Удаляем краевые пробелы
        String trimmedMessage = message.trim();

        // Заменяем символы на '*'
        String maskedMessage = trimmedMessage.replaceAll("[a-zA-Z]", "*");

        return maskedMessage;
    }
    private void writeToFile(String maskedMessage, String originalMessage) throws IOException {
        String filePath = "../../../../../../RIS/L4S45454/src/main/resources/messages.txt";

        FileWriter fileWriter = new FileWriter(filePath, true);
        fileWriter.write(maskedMessage);
        fileWriter.write(System.lineSeparator());
        fileWriter.write(originalMessage);
        fileWriter.write(System.lineSeparator());
        fileWriter.close();
    }
}
