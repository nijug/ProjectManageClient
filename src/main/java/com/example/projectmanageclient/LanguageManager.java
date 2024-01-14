package com.example.projectmanageclient;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static LanguageManager instance = null;

    private final ObjectProperty<ResourceBundle> resourceBundle;
    @Getter
    private final ObservableList<Locale> supportedLocales;

    private LanguageManager() {
        supportedLocales = FXCollections.observableArrayList(
                new Locale("en"),
                new Locale("pl")
        );
        resourceBundle = new SimpleObjectProperty<>(loadResourceBundle( new Locale("en")));
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle.get();
    }

    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public void changeLocale(Locale locale) {
        resourceBundle.set(loadResourceBundle(locale));
    }

    private ResourceBundle loadResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("com/example/projectmanageclient/properties/messages", locale);
    }
}
