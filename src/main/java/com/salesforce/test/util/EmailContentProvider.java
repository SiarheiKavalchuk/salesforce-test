package com.salesforce.test.util;

import com.salesforce.test.core.exceptions.AutomationBugException;
import com.salesforce.test.util.entity.EmailMessageDto;
import com.salesforce.test.util.entity.PasswordAuthenticator;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.core.ConditionTimeoutException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTY_MAIL_EMAIL;
import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTY_MAIL_PASSWORD;
import static org.awaitility.Awaitility.await;

@Slf4j
public final class EmailContentProvider {
    private static final String INBOX = "Inbox";
    private static final String EMAIL_PROPERTIES_PATH = "email.properties";
    private static final String IDENTITY_EMAIL_SUBJECT = "Verify your identity in Salesforce";
    private static final Pattern EXTRACT_VERIFICATION_CODE_PATTERN = Pattern.compile("(\\d{6})");

    private static volatile EmailContentProvider emailContentProvider;

    private final String email;
    private final String emailPassword;
    private Properties emailProperties;
    private Store imapStore;
    private Folder inboxFolder;

    private EmailContentProvider() {
        this.email = PropertiesProvider.getAppPropertyValue(APP_PROPERTY_MAIL_EMAIL);
        this.emailPassword = PropertiesProvider.getAppPropertyValue(APP_PROPERTY_MAIL_PASSWORD);
    }

    public static String getVerificationCode(Instant searchFrom) {
        return getEmailContentProvider().getVerificationCodeFromLastEmail(searchFrom);
    }

    public static void cleanInboxFolderAndCloseConnection() {
        getEmailContentProvider()
                .deleteInboxMessages()
                .closeConnection();
    }

    private static EmailContentProvider getEmailContentProvider() {
        var result = emailContentProvider;
        if (result != null) {
            return result;
        }
        synchronized (EmailContentProvider.class) {
            if (emailContentProvider == null) {
                emailContentProvider = new EmailContentProvider();
            }
            return emailContentProvider;
        }
    }

    private String getVerificationCodeFromLastEmail(Instant searchFrom) {
        var message = getLastVerificationMessageDto(searchFrom);
        var matcher = EXTRACT_VERIFICATION_CODE_PATTERN.matcher(message.getBody());
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new AutomationBugException("Verification code not found in message: \n" + message.getBody());
    }

    private EmailMessageDto getLastVerificationMessageDto(Instant searchFrom) {
        return await("Getting verification message from messages list")
                .pollInSameThread()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(4, TimeUnit.SECONDS)
                .until(() -> getAllMessagesFromInboxFolder().stream()
                        .map(this::getEmailMessageDtoFromImapMessage)
                        .filter(message -> !message.getSentDate().isBefore(searchFrom))
                        .filter(message -> message.getSubject().contains(IDENTITY_EMAIL_SUBJECT))
                        .max(Comparator.comparing(EmailMessageDto::getSentDate))
                        .orElse(null), Objects::nonNull);
    }

    private List<Message> getAllMessagesFromInboxFolder() {
        try {
            return Arrays.stream(getInboxFolder().getMessages()).toList();
        } catch (MessagingException e) {
            throw new AutomationBugException("Can't get messages from inbox folder: " + e.getMessage());
        }
    }

    private EmailMessageDto getEmailMessageDtoFromImapMessage(Message imapMessage) {
        try {
            return EmailMessageDto.builder()
                    .subject(imapMessage.getSubject())
                    .body((String) imapMessage.getContent())
                    .sentDate(imapMessage.getSentDate().toInstant())
                    .build();
        } catch (MessagingException | IOException e) {
            log.warn("Can't convert imap message to custom message DTO, reason: {}", e.getMessage());
            return EmailMessageDto.builder()
                    .subject(StringUtils.EMPTY)
                    .body(StringUtils.EMPTY)
                    .sentDate(Instant.EPOCH)
                    .build();
        }
    }

    private Folder getInboxFolder() {
        openImapSessionIfNeeded();
        if (inboxFolder == null || !inboxFolder.isOpen()) {
            try {
                inboxFolder = imapStore.getFolder(INBOX);
                inboxFolder.open(Folder.READ_WRITE);
            } catch (MessagingException e) {
                log.warn("Can't open {} folder, reason: {}", INBOX, e.getMessage());
            }
        }
        return inboxFolder;
    }

    private void openImapSessionIfNeeded() {
        if (isImapStoreNotActive()) openImapStoreSession(email, emailPassword);
    }

    private void openImapStoreSession(String email, String password) {
        var authenticator = new PasswordAuthenticator(email, password);
        var imapSession = Session.getDefaultInstance(getMailProperties(), authenticator);
        if (imapSession == null) throw new AutomationBugException("CONNECTION IS FAILED: imap session is NULL");

        try {
            String protocol = "imaps";
            imapStore = imapSession.getStore(protocol);
            imapStore.connect();
            awaitForConnection(true);
        } catch (MessagingException | ConditionTimeoutException e) {
            throw new AutomationBugException("CONNECTION IS FAILED: " + e.getMessage());
        }
    }

    private EmailContentProvider deleteInboxMessages() {
        try {
            Message[] messages = getInboxFolder().getMessages();
            for (Message message : messages) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
            getInboxFolder().close(true);
            log.info("[{}] messages were deleted from {} folder", messages.length, INBOX);
        } catch (MessagingException e) {
            log.warn("Can't delete messages from {} folder, reason: {}", INBOX, e.getMessage());
        }
        return this;
    }

    private void closeConnection() {
        if (isImapStoreNotActive()) return;
        try {
            imapStore.close();
            awaitForConnection(false);
            log.info("Connection closed successfully");
        } catch (MessagingException | ConditionTimeoutException e) {
            log.warn("An error occurs while closing connection: {}", e.getMessage());
        }
    }

    private void awaitForConnection(boolean connect) {
        await().atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> connect == imapStore.isConnected());
    }

    private boolean isImapStoreNotActive() {
        return imapStore == null || !imapStore.isConnected();
    }

    private Properties getMailProperties() {
        if (emailProperties == null) {
            emailProperties = PropertiesProvider.getProperties(EMAIL_PROPERTIES_PATH);
        }
        return emailProperties;
    }

}