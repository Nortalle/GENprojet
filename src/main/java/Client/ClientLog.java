package Client;

import Utils.OTrainProtocol;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ClientLog extends Handler {
    private JTextArea component;

    @Override
    public void publish(LogRecord record) {
        String toPrint = record.getLevel() + ": " + record.getMessage();
        if(component != null) {
            if(record.getMessage().equals(OTrainProtocol.SUCCESS)) {
                component.setText(toPrint);
                component.setForeground(Color.GREEN);
            } else if(record.getMessage().equals(OTrainProtocol.FAILURE)) {
                component.setText(toPrint);
                component.setForeground(Color.RED);
            }
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
