package com.tiu.turk.authentication.web.captcha;

import jakarta.servlet.http.HttpSession;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.SecureRandom;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;

/**
 * Self-hosted image CAPTCHA: the code is drawn server-side with Java2D and kept
 * in the HTTP session, so no external service or API key is needed.
 */
@Service
public class CaptchaService {
    public static final String SESSION_ATTRIBUTE = "AUTH_CAPTCHA_CODE";

    /** Ambiguous characters (0/O, 1/I/L) are excluded on purpose. */
    private static final String ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 5;
    private static final int WIDTH = 170;
    private static final int HEIGHT = 56;

    private final SecureRandom random = new SecureRandom();

    /** Generates a fresh code, stores it in the session and returns the rendered PNG. */
    public byte[] generate(HttpSession session) {
        String code = randomCode();
        session.setAttribute(SESSION_ATTRIBUTE, code);
        return render(code);
    }

    /**
     * One-time check: the stored code is removed no matter the outcome, so a wrong
     * guess (or a replayed form) always requires a newly rendered image.
     */
    public boolean verify(HttpSession session, String answer) {
        if (session == null) {
            return false;
        }
        Object stored = session.getAttribute(SESSION_ATTRIBUTE);
        session.removeAttribute(SESSION_ATTRIBUTE);
        return stored instanceof String code
                && answer != null
                && code.equalsIgnoreCase(answer.trim());
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(this.random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private byte[] render(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(0xEA, 0xF3, 0xFB), WIDTH, HEIGHT, new Color(0xD3, 0xE5, 0xF5)));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Background noise: thin lines and dots in muted brand tones.
            for (int i = 0; i < 6; i++) {
                g.setColor(new Color(54, 52, 142, 40 + this.random.nextInt(50)));
                g.setStroke(new BasicStroke(1.2f));
                g.drawLine(this.random.nextInt(WIDTH), this.random.nextInt(HEIGHT),
                        this.random.nextInt(WIDTH), this.random.nextInt(HEIGHT));
            }
            for (int i = 0; i < 60; i++) {
                g.setColor(new Color(54, 52, 142, 30 + this.random.nextInt(60)));
                g.fillOval(this.random.nextInt(WIDTH), this.random.nextInt(HEIGHT), 2, 2);
            }

            int charBox = (WIDTH - 20) / CODE_LENGTH;
            for (int i = 0; i < code.length(); i++) {
                Graphics2D cg = (Graphics2D) g.create();
                try {
                    int size = 28 + this.random.nextInt(8);
                    cg.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size));
                    cg.setColor(new Color(30 + this.random.nextInt(40), 28 + this.random.nextInt(40), 100 + this.random.nextInt(60)));
                    int x = 12 + i * charBox + this.random.nextInt(6);
                    int y = 38 + this.random.nextInt(10);
                    double angle = Math.toRadians(this.random.nextInt(40) - 20);
                    cg.rotate(angle, x, y);
                    cg.drawString(String.valueOf(code.charAt(i)), x, y);
                } finally {
                    cg.dispose();
                }
            }
        } finally {
            g.dispose();
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to render captcha image", e);
        }
    }
}
