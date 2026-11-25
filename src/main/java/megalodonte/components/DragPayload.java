package megalodonte.components;

import javafx.scene.input.DataFormat;

final class DragPayload {
    static final DataFormat FORMAT = new DataFormat("application/x-megalodonte-component-payload");
    private DragPayload() {}
}
