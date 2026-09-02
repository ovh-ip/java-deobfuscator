package com.deobfuscator;

import com.deobfuscator.transformers.XorStringDecryptor;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class XorTest {
    @Test
    public void testXorDecrypt() {
        ClassNode cn = new ClassNode();
        MethodNode mn = new MethodNode(0, "test", "()V", null, null);
        mn.instructions.add(new LdcInsnNode("ifmmp"));
        mn.instructions.add(new MethodInsnNode(16777216, "a", "decrypt", "(Ljava/lang/String;)Ljava/lang/String;", false));
        cn.methods = List.of(mn);
        new XorStringDecryptor().transform(cn);
        assertTrue(mn.instructions.get(0) instanceof LdcInsnNode);
        assertEquals("hello", ((LdcInsnNode) mn.instructions.get(0)).cst);
    }
}
