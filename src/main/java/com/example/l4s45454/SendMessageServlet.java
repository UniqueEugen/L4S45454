package com.example.l4s45454;

import jakarta.annotation.Resource;
import jakarta.jms.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(mappedName = "jms/MyQueue")
    private Queue queue;

    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer =
                    session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            for (int i = 0; i < 10; i++) {
                message.setText("This is message " + (i + 1));
                System.out.println("Sending message: " +
                        message.getText());
                messageProducer.send(message); }

            response.getWriter().println("Message successfully sended: " + message);
        } catch (Exception e) {
            response.getWriter().println("Error during sending message: " + e.getMessage());
        }
    }
}