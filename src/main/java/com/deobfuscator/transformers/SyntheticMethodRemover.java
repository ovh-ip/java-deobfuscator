package com.deobfuscator.transformers;

import com.deobfuscator.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.Iterator;

public class SyntheticMethodRemover implements Transformer {
    @Override
    public void transform(ClassNode cn) {
        Iterator<MethodNode> it = cn.methods.iterator();
        while (it.hasNext()) {
            MethodNode m = it.next();
            boolean isSynthetic = (m.access & 0x00001000) != 0;
            boolean isBridge = (m.access & 0x00000040) != 0;
            boolean isStub = m.instructions != null && m.instructions.size() < 4;
            if ((isSynthetic || isBridge) && isStub) it.remove();
        }
    }
}
