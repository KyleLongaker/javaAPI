package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;
    private final AccountDAO accountDAO;

    // Default constructor
    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    // Constructor for injecting DAOs (useful for testing)
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // Post a message after validating input and user existence
    public Message postMessage(Message message) {
        if (!isValidMessage(message)) {
            return null;
        }

        Account author = accountDAO.findById(message.getPosted_by());
        if (author == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Get a message by ID
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    // Delete a message by ID
    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    // Update message text if valid
    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }

        boolean updated = messageDAO.updateMessage(messageId, newText);
        if (updated) {
            return messageDAO.getMessageById(messageId);
        }

        return null;
    }

    // Get all messages posted by a specific account
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

    // Helper: validate message constraints
    private boolean isValidMessage(Message message) {
        if (message == null) return false;
        String text = message.getMessage_text();
        return text != null && !text.isBlank() && text.length() <= 255 && message.getPosted_by() > 0;
    }
}
