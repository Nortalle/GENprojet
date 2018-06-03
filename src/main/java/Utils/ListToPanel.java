package Utils;

import javax.swing.*;

public interface ListToPanel<T> {
    JComponent toAdd(T t);
}
