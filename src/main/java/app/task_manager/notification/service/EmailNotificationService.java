package app.task_manager.notification.service;

import app.task_manager.task.entity.TaskEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;       // <-- wstrzyknięte pole

    public void sendTaskAssignmentEmail(String to, TaskEntity task) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("New task assigned: " + task.getTitle());
            msg.setText("Hello,\n\nYou have been assigned to task \""
                    + task.getTitle() + "\".\nView: https://task_manager_app/tasks/"
                    + task.getId());
            mailSender.send(msg);
            log.info("Email sent to {} about task {}", to, task.getId());
        } catch (MailException ex) {
            log.error("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }
}