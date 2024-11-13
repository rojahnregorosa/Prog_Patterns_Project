package org.example.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle resourceBundle;
    private Locale currentLocale;

    private LanguageManager() {
        setLanguage("en"); // Default language
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals("fr")) {
            currentLocale = Locale.FRANCE;  // French locale
        } else {
            currentLocale = Locale.US;  // Default to English locale
        }
        resourceBundle = ResourceBundle.getBundle("messages.Messages", currentLocale);
    }

    public String getMessage(String key, Object... args) {
        try {
            String message = resourceBundle.getString(key);
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            System.out.println("Missing translation for key: " + key);
            return key;
        }
    }
}
