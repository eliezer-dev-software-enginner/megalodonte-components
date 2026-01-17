# megalodonte-components

Biblioteca de componentes JavaFX reativos para o ecossistema Megalodonte. Oferece uma API moderna e fluente para criar interfaces de usuário com estado reativo.

## ✨ Features

- **Componentes Reativos**: Componentes que reagem automaticamente a mudanças de estado
- **API Fluente**: Sintaxe encadeada para construção de layouts
- **Static Factories**: Crie componentes sem usar `new` keyword
- **Props System**: Sistema de propriedades para customização
- **Theming**: Sistema de temas embutido
- **Type Safety**: Totalmente tipado com generics

## 📦 Instalação

Após publicar localmente:

```bash
./gradlew publishToMavenLocal
```

Adicione ao seu projeto:

```gradle
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

## 🚀 Uso Básico

### Com constructors tradicionais:

```java
import megalodonte.components.*;

Column column = new Column()
    .c_child(new Text("Hello World"))
    .c_child(new Button("Click me"));
```

### Com static factories (recomendado):

```java
import static megalodonte.components.statics.Components.*;

Column column = Column()
    .c_child(Text("Hello World"))
    .c_child(Button("Click me"));
```

### Com métodos `.of()`:

```java
import megalodonte.components.*;

Column column = Column.of()
    .c_child(Text.of("Hello World"))
    .c_child(Button.of("Click me"));
```

## 🔄 Componentes Reativos

```java
import megalodonte.State;
import static megalodonte.components.statics.Components.*;

// Estado reativo
State<String> name = State.of("John Doe");
State<String> buttonText = State.of("Click me!");

// Componentes que reagem ao estado
Component ui = Column()
    .c_child(Text(name))  // Atualiza automaticamente
    .c_child(Button(buttonState));
```

## 🧩 Componentes Disponíveis

### Layout
- **Column**: Layout vertical
- **Row**: Layout horizontal  
- **SpacerVertical**: Espaçador vertical expansível
- **SpacerHorizontal**: Espaçador horizontal expansível

### UI Elements
- **Text**: Texto reativo
- **Button**: Botão com animações
- **Card**: Container com estilo
- **Image**: Componente de imagem
- **ProgressBar**: Barra de progresso reativa

### Inputs
- **Input**: Campo de texto
- **PasswordInput**: Campo de senha
- **TextAreaInput**: Área de texto multilinha
- **Select**: Dropdown select
- **DatePicker**: Seletor de data

### Exemplo completo:

```java
import static megalodonte.components.statics.Components.*;
import megalodonte.State;

State<String> email = State.of("");
State<String> password = State.of("");

Component loginForm = Column()
    .c_child(Text("Login", new TextProps().h2()))
    .c_child(Input(email))
    .c_child(PasswordInput(password))
    .c_child(Button("Login", new ButtonProps().primary()));
```

## 🎨 Props System

Personalize componentes com props:

```java
TextProps titleProps = new TextProps()
    .h1()
    .bold()
    .primary();

ButtonProps primaryButton = new ButtonProps()
    .primary()
    .large();

Component styled = Column()
    .c_child(Text("Title", titleProps))
    .c_child(Button("Submit", primaryButton));
```

## 🔧 Tecnologias

- **Java 17**
- **JavaFX 17.0.10**
- **JUnit 5** (testes)
- **Mockito** (testes)
- **Gradle** (build)

## 📁 Estrutura do Projeto

```
src/
 ├─ main/java/megalodonte/
 │   ├─ components/           # Componentes principais
 │   ├─ components/statics/   # Static factory methods
 │   ├─ components/inputs/    # Componentes de input
 │   ├─ props/               # Sistema de propriedades
 │   └─ styles/              # Sistema de temas
 └─ test/java/megalodonte/   # Testes
```

## 🧪 Testes

```bash
./gradlew test
```

## 📜 Build

```bash
./gradlew build
./gradlew publishToMavenLocal
```

## 👨‍💻 Autor

Projeto desenvolvido por **Megalodonte** como parte do ecossistema de componentes JavaFX reativos.