package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class EmailService {
    
    private static Properties emailConfig;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    static {
        loadEmailConfiguration();
    }
    
    private static void loadEmailConfiguration() {
        emailConfig = new Properties();
        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream("email-config.properties")) {
            if (input != null) {
                emailConfig.load(input);
            } else {
                System.err.println("Email configuration file not found. Using default settings.");
                // Set default values
                emailConfig.setProperty("email.smtp.host", "smtp.gmail.com");
                emailConfig.setProperty("email.smtp.port", "587");
                emailConfig.setProperty("email.from.address", "your-store@gmail.com");
                emailConfig.setProperty("email.from.password", "your-app-password");
                emailConfig.setProperty("store.name", "Flower Store");
                emailConfig.setProperty("store.phone", "+972-XX-XXXXXXX");
            }
        } catch (IOException e) {
            System.err.println("Error loading email configuration: " + e.getMessage());
        }
    }
    
    public static void sendOrderConfirmationEmail(Order order) {
        try {
            // Email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", emailConfig.getProperty("email.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", emailConfig.getProperty("email.smtp.starttls.enable", "true"));
            props.put("mail.smtp.host", emailConfig.getProperty("email.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", emailConfig.getProperty("email.smtp.port", "587"));
            
            // Create session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        emailConfig.getProperty("email.from.address"),
                        emailConfig.getProperty("email.from.password")
                    );
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getProperty("email.from.address")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getCustomerEmail()));
            message.setSubject(emailConfig.getProperty("email.subject.order.confirmation", "Order Confirmation - Flower Store"));
            
            // Create email content
            String emailContent = createOrderConfirmationEmailContent(order);
            message.setText(emailContent);
            
            // Send email
            Transport.send(message);
            
            System.out.println("Order confirmation email sent successfully to: " + order.getCustomerEmail());
            
        } catch (MessagingException e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String createOrderConfirmationEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        
        content.append("Dear ").append(order.getCustomerName()).append(",\n\n");
        content.append("Thank you for your order! Your order has been confirmed and is being processed.\n\n");
        
        content.append("ORDER DETAILS:\n");
        content.append("==============\n");
        content.append("Order ID: ").append(order.getId()).append("\n");
        content.append("Order Date: ").append(dateFormat.format(order.getOrderDate())).append("\n");
        content.append("Status: ").append(order.getStatus()).append("\n\n");
        
        content.append("DELIVERY INFORMATION:\n");
        content.append("=====================\n");
        if (order.isRequiresDelivery()) {
            content.append("Delivery Type: Home Delivery\n");
            content.append("Delivery Address: ").append(order.getStreetAddress())
                   .append(", ").append(order.getCity());
            if (order.getApartment() != null && !order.getApartment().isEmpty()) {
                content.append(", Apt ").append(order.getApartment());
            }
            content.append("\n");
            if (order.getDeliveryTime() != null) {
                content.append("Scheduled Delivery: ").append(dateFormat.format(order.getDeliveryTime())).append("\n");
            }
        } else {
            content.append("Delivery Type: Store Pickup\n");
        }
        content.append("\n");
        
        content.append("ORDER ITEMS:\n");
        content.append("============\n");
        double subtotal = 0.0;
        for (CartItem item : order.getItems()) {
            content.append("• ").append(item.getFlower().getFlowerName())
                   .append(" (").append(item.getFlower().getFlowerType()).append(")")
                   .append(" - Qty: ").append(item.getQuantity())
                   .append(" - Price: ₪").append(String.format("%.2f", item.getFlower().getFlowerPrice()))
                   .append(" - Total: ₪").append(String.format("%.2f", item.getTotalPrice()))
                   .append(" - Store: ").append(item.getStore()).append("\n");
            subtotal += item.getTotalPrice();
        }
        content.append("\n");
        
        content.append("PRICING SUMMARY:\n");
        content.append("================\n");
        content.append("Subtotal: ₪").append(String.format("%.2f", subtotal)).append("\n");
        content.append("Delivery Fee: ₪").append(String.format("%.2f", order.getDeliveryFee())).append("\n");
        content.append("Total Amount: ₪").append(String.format("%.2f", order.getTotalAmount())).append("\n\n");
        
        // Add greeting card information if included
        if (order.isIncludeGreetingCard()) {
            content.append("GREETING CARD:\n");
            content.append("==============\n");
            content.append("Background: ").append(order.getGreetingCardBackground()).append("\n");
            content.append("Message: ").append(order.getGreetingCardMessage()).append("\n\n");
        }
        
        content.append("CONTACT INFORMATION:\n");
        content.append("===================\n");
        content.append("If you have any questions about your order, please contact us:\n");
        content.append("Email: ").append(emailConfig.getProperty("email.from.address")).append("\n");
        content.append("Phone: ").append(emailConfig.getProperty("store.phone")).append("\n\n");
        
        content.append("Thank you for choosing ").append(emailConfig.getProperty("store.name", "our flower store")).append("!\n\n");
        content.append("Best regards,\n");
        content.append(emailConfig.getProperty("store.name", "Flower Store")).append(" Team");
        
        return content.toString();
    }
    
    // Method to test email configuration
    public static boolean testEmailConfiguration() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", emailConfig.getProperty("email.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", emailConfig.getProperty("email.smtp.starttls.enable", "true"));
            props.put("mail.smtp.host", emailConfig.getProperty("email.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", emailConfig.getProperty("email.smtp.port", "587"));
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        emailConfig.getProperty("email.from.address"),
                        emailConfig.getProperty("email.from.password")
                    );
                }
            });
            
            // Test connection
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.close();
            
            System.out.println("Email configuration test successful!");
            return true;
            
        } catch (MessagingException e) {
            System.err.println("Email configuration test failed: " + e.getMessage());
            return false;
        }
    }
    
    // Method to send order status update emails
    public static void sendOrderStatusUpdateEmail(Order order, String newStatus) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", emailConfig.getProperty("email.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", emailConfig.getProperty("email.smtp.starttls.enable", "true"));
            props.put("mail.smtp.host", emailConfig.getProperty("email.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", emailConfig.getProperty("email.smtp.port", "587"));
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        emailConfig.getProperty("email.from.address"),
                        emailConfig.getProperty("email.from.password")
                    );
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getProperty("email.from.address")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getCustomerEmail()));
            
            String subject = "";
            String content = "";
            
            switch (newStatus) {
                case "SHIPPED":
                    subject = emailConfig.getProperty("email.subject.order.shipped", "Your Order Has Been Shipped - Flower Store");
                    content = createOrderShippedEmailContent(order);
                    break;
                case "DELIVERED":
                    subject = emailConfig.getProperty("email.subject.order.delivered", "Your Order Has Been Delivered - Flower Store");
                    content = createOrderDeliveredEmailContent(order);
                    break;
                default:
                    return; // Don't send email for other status updates
            }
            
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            
            System.out.println("Order status update email sent successfully to: " + order.getCustomerEmail());
            
        } catch (MessagingException e) {
            System.err.println("Failed to send order status update email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String createOrderShippedEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        
        content.append("Dear ").append(order.getCustomerName()).append(",\n\n");
        content.append("Great news! Your order has been shipped and is on its way to you.\n\n");
        
        content.append("ORDER DETAILS:\n");
        content.append("==============\n");
        content.append("Order ID: ").append(order.getId()).append("\n");
        content.append("Order Date: ").append(dateFormat.format(order.getOrderDate())).append("\n");
        content.append("Status: SHIPPED\n\n");
        
        if (order.isRequiresDelivery()) {
            content.append("Your order will be delivered to:\n");
            content.append(order.getStreetAddress()).append(", ").append(order.getCity());
            if (order.getApartment() != null && !order.getApartment().isEmpty()) {
                content.append(", Apt ").append(order.getApartment());
            }
            content.append("\n\n");
        }
        
        content.append("Thank you for choosing ").append(emailConfig.getProperty("store.name", "our flower store")).append("!\n\n");
        content.append("Best regards,\n");
        content.append(emailConfig.getProperty("store.name", "Flower Store")).append(" Team");
        
        return content.toString();
    }
    
    private static String createOrderDeliveredEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        
        content.append("Dear ").append(order.getCustomerName()).append(",\n\n");
        content.append("Your order has been successfully delivered! We hope you love your flowers.\n\n");
        
        content.append("ORDER DETAILS:\n");
        content.append("==============\n");
        content.append("Order ID: ").append(order.getId()).append("\n");
        content.append("Order Date: ").append(dateFormat.format(order.getOrderDate())).append("\n");
        content.append("Status: DELIVERED\n\n");
        
        content.append("If you have any questions or concerns about your order, please don't hesitate to contact us.\n\n");
        content.append("Thank you for choosing ").append(emailConfig.getProperty("store.name", "our flower store")).append("!\n\n");
        content.append("Best regards,\n");
        content.append(emailConfig.getProperty("store.name", "Flower Store")).append(" Team");
        
        return content.toString();
    }
    
    // Method to send order cancellation emails
    public static void sendOrderCancellationEmail(Order order) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", emailConfig.getProperty("email.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", emailConfig.getProperty("email.smtp.starttls.enable", "true"));
            props.put("mail.smtp.host", emailConfig.getProperty("email.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", emailConfig.getProperty("email.smtp.port", "587"));
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        emailConfig.getProperty("email.from.address"),
                        emailConfig.getProperty("email.from.password")
                    );
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getProperty("email.from.address")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getCustomerEmail()));
            message.setSubject("Order Cancellation Confirmation - " + emailConfig.getProperty("store.name", "Flower Store"));
            
            String emailContent = createOrderCancellationEmailContent(order);
            message.setText(emailContent);
            
            Transport.send(message);
            
            System.out.println("Order cancellation email sent successfully to: " + order.getCustomerEmail());
            
        } catch (MessagingException e) {
            System.err.println("Failed to send order cancellation email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String createOrderCancellationEmailContent(Order order) {
        StringBuilder content = new StringBuilder();
        
        content.append("Dear ").append(order.getCustomerName()).append(",\n\n");
        content.append("Your order has been cancelled as requested.\n\n");
        
        content.append("ORDER DETAILS:\n");
        content.append("==============\n");
        content.append("Order ID: ").append(order.getId()).append("\n");
        content.append("Order Date: ").append(dateFormat.format(order.getOrderDate())).append("\n");
        content.append("Cancellation Date: ").append(dateFormat.format(order.getCancellationDate())).append("\n");
        content.append("Status: CANCELLED\n\n");
        
        if (order.getCancellationReason() != null && !order.getCancellationReason().isEmpty()) {
            content.append("Cancellation Reason: ").append(order.getCancellationReason()).append("\n\n");
        }
        
        content.append("REFUND INFORMATION:\n");
        content.append("===================\n");
        content.append("Original Order Total: ₪").append(String.format("%.2f", order.getTotalAmount())).append("\n");
        content.append("Refund Amount: ₪").append(String.format("%.2f", order.getRefundAmount())).append("\n");
        content.append("Refund Policy Applied: ").append(order.getRefundPolicyDescription()).append("\n\n");
        
        content.append("REFUND POLICY REMINDER:\n");
        content.append("=======================\n");
        content.append("• More than 3 hours until delivery: 100% refund\n");
        content.append("• Between 1-3 hours until delivery: 50% refund\n");
        content.append("• Less than 1 hour until delivery: No refund\n\n");
        
        content.append("Your refund will be processed within 3-5 business days.\n\n");
        
        content.append("CONTACT INFORMATION:\n");
        content.append("===================\n");
        content.append("If you have any questions about your cancellation or refund, please contact us:\n");
        content.append("Email: ").append(emailConfig.getProperty("email.from.address")).append("\n");
        content.append("Phone: ").append(emailConfig.getProperty("store.phone")).append("\n\n");
        
        content.append("Thank you for choosing ").append(emailConfig.getProperty("store.name", "our flower store")).append("!\n\n");
        content.append("Best regards,\n");
        content.append(emailConfig.getProperty("store.name", "Flower Store")).append(" Team");
        
        return content.toString();
    }
} 