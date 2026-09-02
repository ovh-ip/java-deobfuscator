# java-deobfuscator

Lightweight Java bytecode deobfuscator. Removes common obfuscation patterns without external dependencies.

## Features

- String decryption (XOR, AES stubs, constant pool)
- Control flow deobfuscation
- Synthetic method cleanup
- Line number and local variable restoration
- CLI and library mode

## Installation

```bash
git clone https://github.com/ovh-ip/java-deobfuscator
cd java-deobfuscator
./gradlew build
```

## Usage

CLI:

```bash
java -jar build/libs/java-deobfuscator.jar -i input.jar -o output.jar
```

Library:

```java
Deobfuscator deobfuscator = new Deobfuscator();
deobfuscator.addTransformer(new StringDecryptor());
deobfuscator.addTransformer(new ControlFlowCleaner());
deobfuscator.run(inputJar, outputJar);
```

## Supported patterns

| Obfuscator | Status |
|---|---|
| Zelix KlassMaster | Partial |
| Allatori | Yes |
| ProGuard name obfuscation | Yes |
| Custom XOR string encryption | Yes |

## Structure

```
src/main/java/com/deobfuscator/
  transformers/
  bytecode/
  utils/
```

## License

MIT
