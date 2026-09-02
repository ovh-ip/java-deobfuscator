package com.deobfuscator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import java.io.*;
import java.util.List;
import java.util.jar.*;

public class JarProcessor {
    private final List<Transformer> transformers;

    public JarProcessor(List<Transformer> transformers) {
        this.transformers = transformers;
    }

    public void process(File input, File output) {
        try (JarFile jarFile = new JarFile(input); JarOutputStream jos = new JarOutputStream(new FileOutputStream(output))) {
            jarFile.stream().forEach(entry -> {
                try {
                    InputStream is = jarFile.getInputStream(entry);
                    if (entry.getName().endsWith(".class")) {
                        ClassReader reader = new ClassReader(is);
                        ClassNode node = new ClassNode();
                        reader.accept(node, 0);
                        for (Transformer t : transformers) t.transform(node);
                        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        node.accept(writer);
                        JarEntry newEntry = new JarEntry(entry.getName());
                        jos.putNextEntry(newEntry);
                        jos.write(writer.toByteArray());
                        jos.closeEntry();
                    } else {
                        jos.putNextEntry(new JarEntry(entry.getName()));
                        is.transferTo(jos);
                        jos.closeEntry();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
