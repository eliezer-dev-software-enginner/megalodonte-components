module megalodonte.components {
    requires transitive megalodonte.base;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;

    exports megalodonte.components;
    exports megalodonte.components.inputs;
    exports megalodonte.components.layout_components;
    exports megalodonte.components.v2;
    exports megalodonte.props;

    // NOT exported: megalodonte.styles.util
    //   Only class: megalodonte.styles.util.StyleUtils
    //   Used exclusively by classes inside megalodonte.components / megalodonte.props.
}
