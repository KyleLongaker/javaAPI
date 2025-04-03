package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

public class SocialMediaController {
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Account endpoints
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);

        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);

        return app;
    }

    private void registerUser(Context ctx) {
        Account newAcc = ctx.bodyAsClass(Account.class);
        Account created = accountService.register(newAcc);
        if (created == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(created);
        }
    }

    private void loginUser(Context ctx) {
        Account loginRequest = ctx.bodyAsClass(Account.class);
        Account found = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (found == null) {
            ctx.status(401);
        } else {
            ctx.status(200).json(found);
        }
    }

    private void createMessage(Context ctx) {
        Message msg = ctx.bodyAsClass(Message.class);
        Message created = messageService.postMessage(msg);
        if (created == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(created);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageById(id);
        if (msg == null) {
            ctx.status(200).json("");
        } else {
            ctx.status(200).json(msg);
        }
    }

    private void deleteMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message existing = messageService.getMessageById(id);
        if (existing == null) {
            ctx.status(200).json("");
            return;
        }
        boolean success = messageService.deleteMessage(id);
        if (success) {
            ctx.status(200).json(existing);
        } else {
            ctx.status(200).json("");
        }
    }

    private void updateMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message body = ctx.bodyAsClass(Message.class);
        Message updated = messageService.updateMessage(id, body.getMessage_text());  // ✅ FIXED: Uses correct getter
        if (updated == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(updated);
        }
    }

    private void getMessagesByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.status(200).json(messages);
    }
}
