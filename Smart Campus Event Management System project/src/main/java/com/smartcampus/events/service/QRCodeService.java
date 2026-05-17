package com.smartcampus.events.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeService {

    public String generatePaymentQRCode(String paymentMethod, BigDecimal amount, String transactionId, String eventName) throws WriterException, IOException {
        String qrContent = generateQRContent(paymentMethod, amount, transactionId, eventName);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convert to Base64 for embedding in HTML
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(qrImage, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    private String generateQRContent(String paymentMethod, BigDecimal amount, String transactionId, String eventName) {
        switch (paymentMethod.toLowerCase()) {
            case "upi":
                // UPI format: upi://pay?pa=UPI_ID&pn=NAME&am=AMOUNT&cu=INR&tn=DESCRIPTION
                return String.format("upi://pay?pa=smartcampus@upi&pn=Smart Campus&am=%.2f&cu=INR&tn=Payment for %s - %s",
                    amount, eventName, transactionId);

            case "paypal":
                // PayPal format: https://www.paypal.com/paypalme/username/amount
                return String.format("https://www.paypal.com/paypalme/smartcampus/%.2f?description=Payment for %s",
                    amount, eventName);

            case "credit card":
            case "debit card":
                // For demo purposes, return a generic payment URL
                return String.format("https://smartcampus.edu/payment?amount=%.2f&method=%s&txid=%s&event=%s",
                    amount, paymentMethod.replace(" ", "_"), transactionId, eventName.replace(" ", "%20"));

            case "net banking":
                // Bank transfer URL
                return String.format("https://smartcampus.edu/bank-transfer?amount=%.2f&txid=%s&event=%s",
                    amount, transactionId, eventName.replace(" ", "%20"));

            case "wallet":
                // Digital wallet URL (Google Pay, Apple Pay, etc.)
                return String.format("https://smartcampus.edu/digital-wallet?amount=%.2f&txid=%s&event=%s",
                    amount, transactionId, eventName.replace(" ", "%20"));

            default:
                return String.format("https://smartcampus.edu/payment?amount=%.2f&method=%s&txid=%s&event=%s",
                    amount, paymentMethod.replace(" ", "_"), transactionId, eventName.replace(" ", "%20"));
        }
    }

    public String getPaymentInstructions(String paymentMethod) {
        switch (paymentMethod.toLowerCase()) {
            case "upi":
                return "Scan the QR code with any UPI app (Google Pay, PhonePe, Paytm, etc.) and complete the payment.";
            case "paypal":
                return "Scan the QR code or click to open PayPal and complete the payment.";
            case "credit card":
                return "Use the QR code to access the secure payment gateway and enter your credit card details.";
            case "debit card":
                return "Use the QR code to access the secure payment gateway and enter your debit card details.";
            case "net banking":
                return "Scan the QR code to access your bank's net banking portal and complete the transfer.";
            case "wallet":
                return "Scan the QR code or use Google Pay, Apple Pay, or other digital wallets to complete the payment.";
            default:
                return "Scan the QR code to proceed with payment.";
        }
    }
}