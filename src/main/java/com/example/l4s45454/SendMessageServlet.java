package com.example.cl;

import javax.jms.JMSException;

import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection connection = null;
        Session session = null;
        String text = req.getParameter("text") != null ? req.getParameter("text") : "Hello World";
        try {
            Context ic = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms.ConnectionFactory");
            Queue queue = (Queue) ic.lookup("jms/MyQueue");
            // Получение подключения и сессии JMS
            connection = cf.createConnection();
            session = connection.createSession(false, jakarta.jms.Session.AUTO_ACKNOWLEDGE);

            // Создание сообщения и отправка его в очередь
            javax.jms.MessageProducer producer = session.createProducer(queue);

            for (int i = 0; i < 10; i++) {
                String messageText = "Сообщение " + (i + 1);
                javax.jms.TextMessage message = session.createTextMessage(messageText);
                producer.send(message);
                System.out.println("Отправлено сообщение: " + messageText);
            }
        } catch (javax.jms.JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            // Закрытие ресурсов JMS
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}