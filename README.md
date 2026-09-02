# java-deobfuscator

Java bytecode deobfuscator for reversing common obfuscators. Built on ASM 9.7, no native deps.

Removes string encryption, control flow obfuscation and synthetic traps from real world samples.

## Quick start

```bash
./gradlew build
java -jar build/libs/java-deobfuscator.jar -i obf.jar -o clean.jar --transform strings,cf
```

## Library

```java
Deobfuscator d = new Deobfuscator();
d.addTransformer(new XorStringDecryptor());
d.addTransformer(new ControlFlowCleaner());
d.addTransformer(new SyntheticMethodRemover());
d.run(new File("input.jar"), new File("output.jar"));
```

## What it actually does

- XorStringDecryptor — detects `new String(xor(bytes,key))` and `AES/ECB` stubs, inlines decrypted values via dataflow analysis
- ControlFlowCleaner — removes opaque predicates `if (false) goto`, flattens `goto`-chains
- SyntheticMethodRemover — strips bridge/synthetic methods injected by Zelix/Allatori
- NameCleaner — restores `a/b/c` to readable names when mapping provided

## Supported

| Obfuscator | Strings | Control flow | Notes |
|---|---|---|---|
| ProGuard / R8 | - | - | name only, pass-through |
| Allatori 7+ | yes | yes | tested on 7.2 trial |
| Zelix KlassMaster 13 | partial | yes | string pool v2 |
| Custom XOR/AES | yes | - | heuristic, see tests |

## Tests

```bash
./gradlew test
```

## Structure

```
src/main/java/com/deobfuscator/
  transformers/
    XorStringDecryptor.java
    ControlFlowCleaner.java
    SyntheticMethodRemover.java
  bytecode/
    JarProcessor.java
  utils/
```

## Limitations

Does not break native methods or custom VM dispatchers. For those use dynamic analysis.

## License

MIT
