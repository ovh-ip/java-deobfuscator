package com.deobfuscator;

import org.objectweb.asm.tree.ClassNode;

public interface Transformer {
    void transform(ClassNode classNode);
}
