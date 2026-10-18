# megalodonte-components

Reactive JavaFX component library for the Megalodonte framework: layout containers,
form controls, a data table, menus, and the `Props`/theme system that styles all of
them. Builds on `megalodonte-base` (`Component`, `State`/`ReadableState`,
`ThemeManager`) and `megalodonte-reactivity` (`ListState`, `ForEachState`, `Show`).

Every example below is real code, mostly from `plics-sw`'s `Components.java` and
its screens — not invented for this README.

## Installation (Maven Local)

```bash
./gradlew publishToMavenLocal
```

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("megalodonte:megalodonte-components:1.0.0-beta")
    implementation("megalodonte:megalodonte-reactivity:1.0.0-beta")
    implementation("megalodonte:megalodonte-base:1.0.0-beta")
}
```

## Tests

The JUnit 5 and TestFX suite is enabled in the standard Gradle lifecycle:

```bash
./gradlew test
```

## Core pattern: `Component` + `Props`

Every component wraps a JavaFX `Node` and takes an optional `*Props` object in its
constructor:

```java
new Text("Hello world", new TextProps().fontSize(90))
```

`Props.apply(node)` subscribes to `ThemeManager.state()` and calls the props class's
`applyTheme(node, props, theme)` whenever the theme changes (including once,
immediately, when first applied) — **if no theme is set** (`ThemeManager.setTheme(...)`
was never called), that subscription fires with `theme == null` and every
`Props`-driven style (font size, color, borders, ...) is silently skipped. Always set
a theme once, early in `main()`, before any screen renders:

```java
ThemeManager.setTheme(new DefaultTheme()); // from megalodonte-theme
```

Most `*Props` classes are fluent (`.fontSize(14).bold().borderRadius(6)`, ...) and
fall back to values from the current theme (`theme.colors()`, `theme.typography()`,
`theme.spacing()`, `theme.border()`) for anything you don't set explicitly.

## Layout

### `Container`

Single/multi-child `VBox` wrapper. Hugs its content's height by default — it does
**not** stretch to fill a parent's offered space unless you ask it to.

```java
new Container(new ContainerProps().paddingAll(10).bgColor("#fff")).children(
        Components.FormTitle("Buscar produto"),
        new SpacerVertical(5),
        ...
)
```

`ContainerProps`: `paddingAll/Top/Right/Down/Left(int)`, `spacingOf(int)` (inherited
from `LayoutProps`, see below), `width/minWidth/maxWidth/height/maxHeight(double)`,
`bgColor(String)`, `bgImage(String)` (classpath resource, takes priority over
`bgColor`, stretched to cover/centered), `fillHeight()` (opt in to growing inside a
parent `VBox`), `onClick(Runnable)`.

### `Column` / `Row`

`VBox`/`HBox` wrappers — the two workhorse layout primitives. `.children(...)`,
`.c_child(...)`/`.r_child(...)` (single child, chainable), and several `.items(...)`
overloads: a plain `List<Component>`, a `List<T>` + mapper function, or a reactive
`ForEachState<T, C>` (optionally wrapped in its own internal `ScrollPane` via the
`boolean isScrollable` overload).

```java
return new Column().children(
        Components.FormTitle("LISTA DE PRODUTOS"),
        new SimpleTable<ItemVenda>().fromData(vm.itensCarrinho) /* ... */
);
```

`ColumnProps`/`RowProps` (both extend `LayoutProps<T>`, see below): `bgColor(String)`,
`centerHorizontally()`/`centerVertically()`, `fillWidth()` (`Column`, grow inside a
parent `Row`/`HBox`) / `fillWidth()` (`Row`, same idea via `HBox.setHgrow`),
`fillHeight()` (`Column` only, grow inside a parent `VBox`). `RowProps` also has
`bottomVertically()`/`leftHorizontally()`.

`LayoutProps<T>` (shared base for `Column`/`Row`/`Container`/`FlowRow` props):
`minWidth/maxWidth/width/maxHeight/height(double)`, `spacingOf(int)`,
`paddingAll/Top/Right/Down/Left(int)`, `onClick(Runnable)`.

### `FlowRow`

`FlowPane` wrapper — lays children out left-to-right, wrapping to a new line when it
runs out of width. Real usage, a responsive card grid:

```java
new FlowRow(new FlowRowProps().fillWidth().spacingOf(10))
        .withTransition(Animations::riseIn)
        .items(cardsForEach)
        .children(saudacaoComponent())
```

`FlowRowProps`: `bgColor(String)`, `verticalSpacing(double)` (defaults to the same
value as `spacingOf` if unset), `fillWidth()` (grow inside a parent `Row`/`HBox`
instead of being capped at the default 400px wrap length), `centerHorizontally()`.
`.items(ForEachState, boolean isScrollable)` wraps the reactive items in their own
`ScrollPane` when `true`.

### `GridFlow`

`TilePane` wrapper — `.child(Component)` / `.items(Iterable<T>, Renderer<T>)`. The
source marks it `//Problemático` (author's own note) — prefer `FlowRow` unless you
specifically need `TilePane`'s equal-size-cell layout.

### Spacers & separators

- `SpacerVertical`/`SpacerHorizontal(int spacingUnits)` — fixed-size gap; no-arg
  constructor + `.fill()` makes it expand to absorb all remaining space in a
  `Column`/`Row` (`Priority.ALWAYS`) instead. Both also accept a
  `ReadableState<Integer>` for a reactive size.
- `LineHorizontal` — thin wrapper around `javafx.scene.control.Separator`.
- `TextFlow` — thin wrapper around `javafx.scene.text.TextFlow`, single child.

## Text

```java
new Text("Hello world", new TextProps().fontSize(90))
new Text(someReadableState)                 // reactive: re-renders on change
new Text("Error", new TextProps().variant... // ⚠ removed, see below
```

Constructors: `Text(String)`, `Text(String, TextProps)`,
`Text(ReadableState<String>)`, `Text(ReadableState<String>, TextProps)` — the
`ReadableState` variants subscribe and update the rendered text automatically.

`TextProps` (extends `TextComponentProps<T>`, shared with `Input`/`Button`):
`fontSize(int)` / `fontSize(ReadableState<Integer>)`, `bold()`, `fontWeight(String)`,
`color(String)`/`textColor(String)` (alias), `tone(TextTone)` — `PRIMARY` (default,
`theme.colors().textPrimary()`), `SECONDARY`/`DISABLED`
(`theme.colors().textSecondary()`), `INVERTED` (`theme.colors().background()`).
Without an explicit `fontSize`, text falls back to `theme.typography().body()`.

> **`TextVariant` was removed** (previously `TITLE`/`SUBTITLE`/`BODY`/`SMALL`, a
> shortcut for `theme.typography().resolve(variant)`). Use `.fontSize(...)` with the
> matching theme accessor directly instead:
> ```java
> new Text("Login", new TextProps().fontSize(ThemeManager.theme().typography().subtitle()).bold())
> ```

## Inputs

`PasswordInput` and the legacy single-line `Input` extend `InputBase`, which wraps
their JavaFX text controls in a `StackPane`. `TextAreaInput` extends `Component`
and owns a styled JavaFX `TextArea` directly. **`megalodonte.components.inputs.Input`
is deprecated** (still works — `text-field.css` neutralizes Modena's border/bevel —
but it's still a real `TextField` underneath, so it's still exposed to whatever
Modena does). Use **`megalodonte.components.v2.Input`** for any new single-line
text field: built from scratch on a plain `Pane` (`Text` for content, `Rectangle`
for caret/selection, manual key/mouse handling) — no `Control`/`TextInputControl`
at all underneath, so there's no Modena skin to fight, by construction rather than
by override. The single-line inputs and `PasswordInput` share the
fluent API: `.onChange(Function<String, OnChangeResult>)`, `.onInitialize(...)`
(same shape, runs once when the bound state's initial value is applied — e.g. to
format a raw stored value for display), `.onEnter(Runnable)`, `.left(Node)`/`.right(Node)`
(icon/adornment inside the field), `.lockCursorToEnd()`, `.onChangeFocus(Consumer<Boolean>)`,
`.requestFocus()`. `TextAreaInput` binds a `State<String>` bidirectionally and
exposes `.requestFocus()`, `.onChangeFocus(...)` and `.control()`; it does not
implement the single-line input formatting or Enter handlers.

`OnChangeResult.of(displayValue, stateValue)` lets you show a formatted string
(currency, masked input, ...) while storing a different raw value in the bound
`State<String>` — see the currency example below.

### `v2.Input`

```java
public static Component searchInput(State<String> stateInput, String placeholder) {
    var icon = FontIcon.of(AntDesignIconsOutlined.SEARCH, 20, Color.web(ThemeManager.theme().colors().secondary()));
    return new megalodonte.components.v2.Input(stateInput,
            new megalodonte.props.v2.InputProps().placeHolder(placeholder).width(300).height(31))
            .left(icon);
}
```

Currency formatting via `.onInitialize`/`.onChange`, from `Components.InputColumnCurrency`:

```java
new Input(inputState, inputProps)
        .onInitialize(value -> {
            if (value.matches("\\d+")) {
                BigDecimal realValue = new BigDecimal(value).movePointLeft(2);
                return OnChangeResult.of(BRL.format(realValue), value);
            }
            return OnChangeResult.of(value, value);
        })
        .onChange(value -> {
            String numeric = value.replaceAll("[^0-9]", "");
            if (numeric.isEmpty()) return OnChangeResult.of("R$ 0,00", "0");
            BigDecimal realValue = new BigDecimal(numeric).movePointLeft(2);
            return OnChangeResult.of(BRL.format(realValue), numeric);
        });
```

Getting focus onto a specific input programmatically (e.g. after selecting a search
result) — attach a `Ref<Input>` when building it, then call `.requestFocus()` on it
later:

```java
final Ref<Input> qtdRef = new Ref<>();
// ... Components.InputColumnComEnterHandler(label, state, placeholder, onEnter, qtdRef)
if (qtdRef.current() != null) qtdRef.current().requestFocus();
```

`v2.Input` known gaps (not implemented, unlike a real `TextField`): no mouse
drag-select, no right-click context menu, no IME beyond what `KeyEvent.getCharacter()`
already delivers as-is — worth testing with accented input (ç, ã, õ, ...) before
trusting it for free-text fields. Keyboard select-all/extend (Ctrl+A, Shift+arrows)
and copy/cut/paste (Ctrl+C/X/V) do work.

`megalodonte.props.v2.InputProps`: `placeHolder(String)`, `height/width(int)` —
**without an explicit size, the field is capped at `Region.USE_PREF_SIZE`** instead
of stretching to fill a parent `VBox`/`HBox` or a `fitToHeight` `ScrollPane` (same
reasoning as `InputProps` below — the size constraint has to live on the outer
`StackPane` node that actually goes into the parent's layout, not on the inner
`Pane` alone, or nothing caps how far it can stretch), `disable()`, `tone(TextTone)`,
`bgColor/borderColor(String)`, `borderWidth/borderRadius(int)`, plus everything from
`TextComponentProps` (`fontSize`, `bold`, `color`/`textColor`).

### `Input` (deprecated)

`InputProps`: `placeHolder(String)`, `height/width(int)` — **without an explicit
size, both the field and its wrapping `StackPane` are capped at
`Region.USE_PREF_SIZE`** instead of stretching to fill a parent `VBox`/`HBox` or a
`fitToHeight` `ScrollPane` (that combination used to be able to drive JavaFX's
layout pass into a `StackOverflowError` — see `Components.ScrollPaneDefault`, used
by every CRUD screen's form area), `disable()`, `tone(TextTone)`, `bgColor/borderColor(String)`,
`borderWidth/borderRadius(int)`, plus everything from `TextComponentProps`
(`fontSize`, `bold`, `color`/`textColor`). Also applies to `TextAreaInput` (same
props class, `applyTextAreaTheme` mirrors the same size-capping logic).

### `TextAreaInput`

```java
new TextAreaInput(inputState, getInputProps(placeholder, height).width(400))
```

### `JavaCodeEditor`

Use `new JavaCodeEditor(sourceState, 260)` for editable Java source. It binds to
`State<String>`, follows the Megalodonte theme, displays line numbers and Java
syntax colors, and inserts indentation with Tab and Enter. The component wraps
RichTextFX 0.11.7, which supports JavaFX 25; applications use only the
Megalodonte API.

### Project-oriented desktop helpers

`FlowCanvas` displays draggable trigger/action pairs and exposes `onSelect` and
`onMove` callbacks. Clicking a card selects it without firing a move event; the
selected pair gets a visible border. Dragging empty space pans the unbounded
canvas without scroll bars or changing saved card coordinates. In a horizontal
layout, the canvas grows into available space beside adjacent panels.

`FilePicker.chooseFile(...)` and `FilePicker.chooseDirectory(...)` expose native
pickers through the framework. `FilePicker.openDirectory(path)` opens a project
folder in the platform file manager; applications do not need to create JavaFX
chooser controls themselves.

### `PasswordInput`

Same constructor shape as `Input`/`TextAreaInput`. Adds a 👁 toggle button (via
`.right(...)`) that swaps the underlying control between `PasswordField` and a plain
`TextField` to reveal/hide the value.

### `CurrencyInput`

Explicitly marked in its own source as a *"solução temporária"* (temporary
solution) — a hand-rolled `TextField` wrapper that formats an initial BRL value on
construction, bypassing `InputProps`/theming entirely (hardcodes
`-fx-font-size: 14px; -fx-border-radius: 4px;`). Prefer `Input` +
`.onInitialize`/`.onChange` (see the currency example above) for anything new.

## Buttons & clickable areas

### `Button`

```java
new Button("Increment", btnProps).onClick(() -> counter.set(counter.get() + 1))
new Button(someReadableStateOfString).onClick(handler)  // reactive label
```

Built-in hover/press opacity animation (no configuration needed). `.onClick(Runnable)`,
`.icon(IconInterface)` / `.icon(ReadableState<IconInterface>)` (left-aligned by
default, 6px gap; combine with `ButtonProps.iconOnRight()` to flip it).

`ButtonProps` (extends `TextComponentProps<T>`): variant shortcuts
`primary()`/`secondary()`/`success()`/`warning()`/`danger()`/`ghost()`/`disabled()`
(or `variant(String)` directly — no enum yet, marked `TODO` in source) each map to a
built-in color; `bgColor(String)` / `bgColor(ReadableState<String>)` overrides the
variant color (and stays reactive if given a state); `borderColor/borderWidth/borderRadius`,
`height(int)`, `fillWidth()` (stretch to `Double.MAX_VALUE`), `iconOnRight()`.

### `Clickable`

Wraps *any* `Component` in a `StackPane` with hover/press opacity animation and a
click handler — the general-purpose "make this thing tappable" wrapper, used
internally by `Menu` for its rows:

```java
new Clickable(cardContent, () -> router.spawnWindow(destination))
```

`.setOnClick(Runnable)` (reassign later), `.setEnabled(boolean)`
(disable + mouse-transparent). `enableHapticFeedback()`/`setLongPressDelay(...)` are
declared but currently no-ops (reserved for a future mobile target).

## Selection controls

### Native state-bound controls

`Switch` binds a boolean state to an accessible on/off toggle; `Slider` binds a
finite numeric range to `State<Double>`. `RadioGroup` binds mutually exclusive
choices to `State<String>` with stable values separate from visible labels.
`Accordion` exposes a collapsible
section whose body is another Megalodonte component. `Tabs` renders a list of
named Megalodonte pages and synchronizes the selected index with
`State<Integer>`. These controls use the active Megalodonte theme and keep
JavaFX nodes behind the framework API.

```java
var enabled = State.of(false);
var volume = State.of(25.0);
var selected = State.of(0);
new Switch("Notifications", enabled);
new Slider(0, 100, volume);
new RadioGroup("Billing frequency", List.of(
        new RadioGroup.Option("monthly", "Monthly"),
        new RadioGroup.Option("weekly", "Weekly")), State.of("monthly"));
new Accordion("Advanced settings", new Text("Details"), State.of(false));
new Tabs(List.of(new Tabs.Page("General", new Text("Overview"))), selected);
```

Set a theme through `ThemeManager` before constructing these controls. The
state subscriptions belong to the view lifecycle, so discard the controls when
the owning screen closes.

### `Select<T>`

Themed `ComboBox<T>` wrapper — including the dropdown popup's cells, which plain
JavaFX CSS can't reach (it renders in its own `Scene`).

### Charts

`BarChartView` and `PieChartView` keep JavaFX chart nodes inside the framework.
Applications supply `State<List<BarChartView.Bar>>` or
`State<List<PieChartView.Slice>>`; changing the state updates the visible chart.
Bar labels and colors are data, so application view models do not need JavaFX
`XYChart.Data` or `PieChart.Data` types.

```java
public static <T> Component SelectColumn(String label, State<List<T>> listState, State<T> stateSelected, Function<T, String> display) {
    return new Column()
            .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
            .c_child(new Select<T>(selectProps)
                    .items(listState)
                    .value(stateSelected)
                    .displayText(display)
            );
}
```

`.items(Iterable<T>)` / `.items(State<List<T>>)` / `.items(ReadableState<List<T>>)`
(reactive), `.value(State<T>)` (two-way bind the selected value),
`.displayText(Function<T, String>)` (custom label per item, replaces the default
`toString()`), `.itemComparator(BiPredicate<T, T>)` / `.compareById()` (match items
by a reflectively-read `id` field instead of `equals()` — handy for entities that
don't implement it), `.expandWhen(ReadableState<Boolean>)` (open/close the popup
programmatically).

`SelectProps` (extends `TextComponentProps<T>`): `bgColor/borderColor/borderWidth/borderRadius`,
`height(int)`, `minWidth/maxWidth(double)`, `paddingAll/Top/Right/Down/Left(int)`,
`disable()`, `tone(TextTone)`.

### `Checkbox`

Themed `CheckBox` wrapper, two-way bound to a `State<Boolean>`. Unlike a plain
`javafx.scene.control.CheckBox`, the box and checkmark are recolored from the
current theme (`theme.colors().primary()` when checked, `theme.colors().surface()`
otherwise, border from `theme.colors().border()`) instead of the OS-native look:

```java
new Checkbox("É uma venda fiada?", vm.isVendaFiada)
```

Combine with `ForEachState` + `FlowRow`/`Column`'s `.items(...)` for a reactive list
of checkboxes instead of manually rebuilding rows on every change, from
`ProdutoScreen.coresCheckboxes()`:

```java
var checkboxesPorCor = ForEachState.of(vm.cores, this::corCheckbox);

return new Column().children(
        new Text("Cores"),
        new FlowRow(new FlowRowProps().spacingOf(8)).items(checkboxesPorCor)
);
```

`CheckboxProps` (extends `TextComponentProps<T>`): `fontSize`, `bold`,
`color`/`textColor` (applies to the label text; the box/mark colors always come
from the theme and aren't customizable yet).

### `DatePicker`

```java
var datePicker = new DatePicker(localDateState,
        new DatePickerProps().fontSize(ThemeManager.theme().typography().small()).height(31)
                .placeHolder("dd/mm/yyyy")
                .locale(new Locale("pt", "BR"))
                .pattern("dd/MM/yyyy")
                .width(140)
                .editable(false)
);
datePicker.icon(calendarIcon); // replaces the default calendar glyph in the arrow button
```

`DatePickerProps`: `formatter(DateTimeFormatter)` / `pattern(String[, Locale])`
(shortcut that builds one), `locale(Locale)`, `placeHolder(String)`,
`editable(boolean)`, `value(State<LocalDate>)`, `height/width(int)`,
`bgColor/borderColor/borderWidth/borderRadius`, `color(String)`. Also fixes Modena's
multi-layer bevel/focus-glow (which otherwise renders as a slightly stepped/uneven
border on a themed color) and re-adds focus feedback using
`theme.colors().focusRing()` instead of Modena's default blue.
`DatePicker.brazilian()`/`.brazilian(State<LocalDate>)` are deprecated convenience
factories (`pt-BR`, `dd/MM/yyyy`, non-editable) — build the equivalent `DatePickerProps`
directly instead.

## Data table: `SimpleTable<T>`

Themed `TableView<T>` with a fluent column builder, reactive data binding, zebra
striping, hover/selection row coloring, and image thumbnails.

```java
new SimpleTable<ItemVenda>()
        .fromData(vm.itensCarrinho)
        .header()
            .columns()
                .imageColumn("Imagem", it -> it.produto.getImagem())
                .column("Cod", it -> it.produto.getCodigoBarras())
                .column("Nome", it -> it.produto.getDescricao())
                .editableColumn("Qtd.", it -> it.quantidade,
                        (it, val) -> vm.atualizarQuantidade(it, new BigDecimal(val)))
                .column("Total", ItemVenda::totalItem)
            .end()
        .build()
        .onItemSelectChange(vm::onSelect)
        .onItemDoubleClick(it -> Components.ShowModal(ItemDetails(it), ctx, 550));
```

- `.fromData(ReadableState<List<T>>)` — table rebuilds its `ObservableList` whenever
  the state changes.
- `.column(title, T -> Object[, Double maxWidth])` — plain text column, extractor
  exceptions are swallowed and render as blank instead of crashing the row.
- `.imageColumn(title, T -> String path[, double size])` — thumbnail column;
  `path` is anything `javafx.scene.image.Image` accepts directly (`file:///...`,
  `http(s)://...`, or a classpath resource path). Loaded images are cached per table
  by path (`TableView` recycles cells while scrolling — without the cache, the same
  image would be re-decoded from disk on every scroll pass); a failed load isn't
  cached, so it retries next time. Size defaults to 40px.
- `.editableColumn(title, extractor, BiConsumer<T, String> onCommit)` — `onCommit`
  fires on Enter or focus loss with the item and the new raw string value.
- `.onItemSelectChange(Consumer<T>)`, `.onItemDoubleClick(Consumer<T>)`,
  `.onChangeFocus(Consumer<Boolean>)`, `.onClickOutside(Runnable)` (click on empty
  table area/header, not on a row).
- `.getItems()`/`.getSelectedItem()`/`.clear()`/`.addItem(T)`/`.removeItem(T)` for
  direct (non-reactive) manipulation alongside `.fromData(...)`.

`SimpleTableProps`: `bgColor`, `headerBgColor`/`headerTextColor`,
`borderColor`/`borderWidth`/`borderRadius`, `striped(boolean)`,
`rowEvenColor`/`rowOddColor`/`rowHoverColor`/`selectionColor`/`rowTextColor`/`separatorColor`,
`headerHeight(int)`, `maxWidth(double)`.

Com `.horizontalScroll()`, as colunas preservam como largura mínima o maior valor entre o
cabeçalho e o conteúdo visível. Quando a tabela é mais larga que essa soma, o espaço excedente
é distribuído proporcionalmente entre as colunas; quando é menor, as larguras mínimas são
mantidas e a rolagem horizontal é exibida.

## Menus: `Menu` / `MenuBar` / `MenuItem`

A dropdown menu system built entirely from this library's own themed components
(`Container`/`Card`/`Text`/`Clickable`) instead of `javafx.scene.control.Menu` —
the source notes the native control's clickable area doesn't always cover the full
row, and a custom popup follows the app's theme instead of the OS look.

```java
var suporteMenu = new Menu("Suporte")
        .item("Relatar erro", () -> ctx.router().spawnWindow(AppRoutes.Screens.RELATAR_ERRO.name(), e -> {}))
        .item("Sugerir melhoria/funcionalidade", () -> ctx.router().spawnWindow(AppRoutes.Screens.SUGERIR_MELHORIA.name(), e -> {}));

return new MenuBar()
        .menu(new Menu("Preferências").item("Abrir tela", () -> ctx.router().spawnWindow(AppRoutes.Screens.PREFERENCIAS.name(), e -> {})))
        .menu(new Menu("Cadastros")
                .item("Fornecedores", () -> ctx.router().spawnWindow(AppRoutes.Screens.FORNECEDORES.name(), e -> {}))
                .item("Clientes", () -> ctx.router().spawnWindow(AppRoutes.Screens.CLIENTES.name(), e -> {})))
        .menuIf(isAdmin, new Menu("Administração")
                .item("Usuários", () -> ctx.router().spawnWindow(AppRoutes.Screens.USUARIOS.name(), e -> {})))
        .menu(suporteMenu);
```

`Menu` is *not* a `Component` itself (it manages its own `Popup`, shown/hidden on
click) — it only becomes visible once added to a `MenuBar`, which renders each
menu's title as its trigger in an `HBox`. `MenuItem(String, Runnable)` — title +
action; `MenuBar`'s `.menu(String title, MenuItem... items)` overload builds the
`Menu` for you inline. Use `.menuIf(condition, menu)` when the complete menu,
including its title, must be omitted when a permission or feature flag is false.

## Media & feedback

- **`Image`** — `ImageView` wrapper. `Image(String path)`, or
  `Image(ReadableState<String> path)` for a reactive source (swaps the underlying
  `javafx.scene.image.Image` whenever the state changes; empty/null hides it).
  `ImageProps`: `size(double)` (shortcut for width+height), `width/height(double)`,
  `preserveRatio(boolean)` (default `true`).
- **`Icon`** — loads an image from a classpath resource at a fixed size, exposed as
  an `IconInterface` for `Button.icon(...)`/`DatePicker.icon(...)`.
- **`ProgressBar`** — wraps `javafx.scene.control.ProgressBar`, bound one-way to a
  `State<Integer>` in the 0–100 range (normalized to 0.0–1.0 internally).

## Scroll behavior

`Scroll` wraps a `Component` in a themed, `fitToWidth` `ScrollPane` with
`VBox.setVgrow(ALWAYS)`. Its static helper,
`Scroll.confineScrollEvents(Node)`, stops a scroll event from bubbling up to an
ancestor `ScrollPane`/`TableView` once the inner one has nothing left to scroll —
without it, scrolling fast to the bottom of a nested table/list also scrolls the
outer page. `SimpleTable`, `Column`/`Row`/`FlowRow`'s scrollable `.items(...)`
overloads, and `Components.ScrollPaneDefault` all call it internally; call it
yourself if you build a custom `ScrollPane`.

## Technologies

- Java 25
- JavaFX
- JUnit 5 + Mockito (tests)
- Gradle with Kotlin DSL

## License

MIT License

## Author

Developed by **Eliezer**.


### Horizontal Scroll v2

`new megalodonte.components.v2.HorizontalScroll(content)` clips content inside
native panes and draws its own scrollbar rather than using `ScrollPane`. It
handles wheel and horizontal gestures, dragging, and Left/Right/Home/End keys.
`scrollTo`, `getScrollX`, `reveal`, `getContent` and `refresh` control position,
reveal a child and update dimensions after content changes. Vertical Scroll v2
provides `scrollTo`, `getScrollY` and `getContent`.

## Data-driven desktop controls

`DataList` is a virtualized, keyboard-accessible list of `Item(label, detail,
badge)`. Use `setItems` to replace rows, `select` to synchronize a selection,
and `onSelect` for user selection. `setItems` and `select` do not invoke the
business callback. Call `close` when the owning view is disposed.

`DiffView` renders a unified diff in a virtualized list. `setText` replaces the
diff; `kind` classifies header, hunk, added, removed and context lines. Users can
select lines and copy either the selection or the complete diff. Call `close`
when the view is disposed.

`ImageSequence` accepts ordered `Item(duration, source, label)` values and
reports selected indices through `onSelect`. `SoundSequence` accepts timed
`Item(start, duration, volume, label)` clips and places overlapping clips in
separate lanes. Both are framework components; callers supply data and handle
domain actions. Both expose `select` and `close`.
