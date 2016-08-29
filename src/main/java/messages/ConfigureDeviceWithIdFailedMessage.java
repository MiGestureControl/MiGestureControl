package messages;

/**
 * Created by hagen on 24/05/16.
 */

/**
 * Diese Nachricht dient zur Signaliserung einer fehlgeschlagenen Konfiguration für ein bestimmtes Gerät und wird vom  HTTPServer behandelt
 */
public class ConfigureDeviceWithIdFailedMessage {
    /**
     * die id des Gerätes
     */
    public final String id;

    /**
     * Konstruktor für die Nachricht erhält die Id des zu konfigurierden Gerätes
     * @param id
     */
    public ConfigureDeviceWithIdFailedMessage(String id) {
        this.id = id;
    }
}
