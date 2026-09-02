package com.deobfuscator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Deobfuscator {
    private final List<Transformer> transformers = new ArrayList<>();

    public void addTransformer(Transformer t) {
        transformers.add(t);
    }

    public void run(File input, File output) {
        JarProcessor processor = new JarProcessor(transformers);
        processor.process(input, output);
    }
}
