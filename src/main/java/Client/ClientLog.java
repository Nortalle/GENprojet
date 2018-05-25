package Client;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ClientLog extends Handler {
    private JTextArea component;

    @Override
    public void publish(LogRecord record) {
        String toPrint = record.getLevel() + ": " + record.getMessage();
        if(component != null) {
            component.setText(toPrint);
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

    public void setComponent(JTextArea component) {
        this.component = component;
    }
}
