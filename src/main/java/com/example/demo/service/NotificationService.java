package com.example.demo.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.repository.ProfileRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final ProfileRepo profileRepo;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    /**
     * Daily reminder at 10 PM IST
     */
    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Kolkata")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepo.findAll();

        for (ProfileEntity profile : profiles) {
            String userName = profile.getFullName();
            String userEmail = profile.getEmail();

            String subject = "Daily Income & Expense Reminder";
            String body = "<html>"
                    + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h3>Hello " + userName + ",</h3>"
                    + "<p>This is a friendly reminder to update your income and expenses for today.</p>"
                    + "<p>💰 Keeping your records updated daily will help you track your financial health better.</p>"
                    + "<a href='" + frontendUrl + "/login' "
                    + "style='display:inline-block; padding:10px 20px; margin-top:10px; "
                    + "background-color:#4CAF50; color:white; text-decoration:none; "
                    + "border-radius:5px;'>Login to Money Manager</a>"
                    + "<p style='margin-top:20px;'>Thank you,<br/>Money Manager Team</p>"
                    + "</body>"
                    + "</html>";

            try {
                emailService.sendEmail(userEmail, subject, body);
                log.info("Reminder email sent to: {}", userEmail);
            } catch (Exception e) {
                log.error("Failed to send reminder to {}: {}", userEmail, e.getMessage());
            }
        }
        log.info("Job completed: sendDailyIncomeExpenseReminder()");
    }

    /**
     * Daily expense summary at 11 PM IST
     */
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Kolkata")
    public void sendDailyExpenseSummary() {
        log.info("Job started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepo.findAll();

        for (ProfileEntity profile : profiles) {
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
            List<ExpenseDTO> todayExpenses = expenseService.getExpenseForUserDate(profile.getId(), today);

            log.info("Profile ID: {}, Today's Date: {}, Expenses Count: {}",
                    profile.getId(), today, todayExpenses.size());

            if (!todayExpenses.isEmpty()) {
                // Build HTML table
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;font-family:Arial,sans-serif;'>")
                        .append("<thead style='background-color:#4CAF50;color:white;'>")
                        .append("<tr>")
                        .append("<th style='padding:10px;text-align:left;border:1px solid #ddd;'>#</th>")
                        .append("<th style='padding:10px;text-align:left;border:1px solid #ddd;'>Name</th>")
                        .append("<th style='padding:10px;text-align:left;border:1px solid #ddd;'>Amount</th>")
                        .append("<th style='padding:10px;text-align:left;border:1px solid #ddd;'>Category</th>")
                        .append("<th style='padding:10px;text-align:left;border:1px solid #ddd;'>Date</th>")
                        .append("</tr>")
                        .append("</thead>")
                        .append("<tbody>");

                int counter = 1;
                for (ExpenseDTO expense : todayExpenses) {
                    table.append("<tr style='background-color:")
                            .append(counter % 2 == 0 ? "#f9f9f9" : "#ffffff").append(";'>")
                            .append("<td style='padding:10px;border:1px solid #ddd;'>").append(counter++).append("</td>")
                            .append("<td style='padding:10px;border:1px solid #ddd;'>").append(expense.getName()).append("</td>")
                            .append("<td style='padding:10px;border:1px solid #ddd;color:#E53935;font-weight:bold;'>₹")
                            .append(expense.getAmount()).append("</td>")
                            .append("<td style='padding:10px;border:1px solid #ddd;'>")
                            .append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A").append("</td>")
                            .append("<td style='padding:10px;border:1px solid #ddd;'>").append(expense.getDate()).append("</td>")
                            .append("</tr>");
                }

                table.append("</tbody></table>");

                String subject = "📊 Daily Expense Summary - " + today;

                String body = "<html><body style='font-family:Arial,sans-serif;line-height:1.6;background-color:#f4f4f4;padding:20px;'>"
                        + "<div style='max-width:600px;margin:auto;background:#ffffff;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.1);padding:20px;'>"
                        + "<h2 style='color:#4CAF50;text-align:center;'>Daily Expense Summary</h2>"
                        + "<p>Hi <b>" + profile.getFullName() + "</b>,</p>"
                        + "<p>Here’s a quick summary of your expenses for today:</p>"
                        + table
                        + "<p style='margin-top:20px;'>Keep tracking your expenses daily to manage your budget better! 💰</p>"
                        + "<div style='text-align:center;margin-top:20px;'>"
                        + "<a href='" + frontendUrl + "/login' style='background:#4CAF50;color:white;padding:10px 20px;text-decoration:none;"
                        + "border-radius:5px;font-weight:bold;'>Login to Money Manager</a>"
                        + "</div>"
                        + "<hr style='margin:30px 0;border:none;border-top:1px solid #eee;'>"
                        + "<p style='font-size:12px;color:#888;text-align:center;'>This is an automated email from Money Manager. Please do not reply.</p>"
                        + "</div>"
                        + "</body></html>";

                try {
                    emailService.sendEmail(profile.getEmail(), subject, body);
                    log.info("Daily summary sent to: {}", profile.getEmail());
                } catch (Exception e) {
                    log.error("Failed to send summary to {}: {}", profile.getEmail(), e.getMessage());
                }
            }
        }

        log.info("Job completed: sendDailyExpenseSummary()");
    }
}
