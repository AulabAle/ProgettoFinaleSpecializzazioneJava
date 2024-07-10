package it.aulab.spec_prog_finale.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}