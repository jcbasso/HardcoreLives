package me.jcbasso.hardcorelives.i18n;

import me.jcbasso.hardcorelives.HardcoreLives;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    private static final String MESSAGES = "messages";
    private final ResourceBundle resourceBundle;

    public Messages(FileConfiguration config, HardcoreLives hl) {
        String locale = config.getString("locale", "en-US");
        this.resourceBundle = ResourceBundle.getBundle(MESSAGES, Locale.forLanguageTag(locale), new FileResClassLoader(Messages.class.getClassLoader(), hl));
    }

    public String getString(String key, Object... args) {
        return MessageFormat.format(resourceBundle.getString(key), args);
    }

    public Object getObject(String key) {
        return resourceBundle.getObject(key);
    }

    /**
     * Attempts to load properties files from the plugin directory before falling back to the jar.
     */
    private static class FileResClassLoader extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(final ClassLoader classLoader, final HardcoreLives hl) {
            super(classLoader);
            this.dataFolder = hl.getDataFolder();
        }

        @Override
        public URL getResource(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                } catch (final MalformedURLException ignored) {
                }
            }
            return null;
        }

        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (final FileNotFoundException ignored) {
                }
            }
            return null;
        }
    }
}
