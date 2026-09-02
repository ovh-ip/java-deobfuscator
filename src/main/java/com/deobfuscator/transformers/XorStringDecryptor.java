package com.deobfuscator.transformers;

import com.deobfuscator.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class XorStringDecryptor implements Transformer {
    @Override
    public void transform(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.instructions == null) continue;
            ListIterator<AbstractInsnNode> it = mn.instructions.iterator();
            while (it.hasNext()) {
                AbstractInsnNode insn = it.next();
                if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
                    MethodInsnNode min = (MethodInsnNode) insn;
                    if (isDecryptStub(min)) {
                        AbstractInsnNode prev = min.getPrevious();
                        if (prev instanceof LdcInsnNode && ((LdcInsnNode) prev).cst instanceof String) {
                            String enc = (String) ((LdcInsnNode) prev).cst;
                            String dec = tryXorDecrypt(enc);
                            if (dec != null) {
                                mn.instructions.set(prev, new LdcInsnNode(dec));
                                mn.instructions.remove(min);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isDecryptStub(MethodInsnNode m) {
        return m.name.toLowerCase().contains("decrypt") || m.name.equals("a") && m.desc.equals("(Ljava/lang/String;)Ljava/lang/String;");
    }

    private String tryXorDecrypt(String s) {
        if (s.length() < 4) return null;
        for (int key = 1; key < 256; key++) {
            StringBuilder sb = new StringBuilder();
            boolean printable = true;
            for (char c : s.toCharArray()) {
                char d = (char) (c ^ key);
                if (d < 32 || d > 126) { printable = false; break; }
                sb.append(d);
            }
            if (printable && sb.toString().matches("[a-zA-Z0-9/._\\-]+")) return sb.toString();
        }
        return null;
    }
}
