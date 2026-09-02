package com.deobfuscator.transformers;

import com.deobfuscator.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ControlFlowCleaner implements Transformer {
    @Override
    public void transform(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.instructions == null) continue;
            int removed = 0;
            for (AbstractInsnNode insn = mn.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn.getOpcode() == Opcodes.GOTO) {
                    JumpInsnNode jmp = (JumpInsnNode) insn;
                    if (jmp.label.getNext() != null && jmp.label.getNext().getOpcode() == Opcodes.GOTO) {
                        mn.instructions.remove(jmp);
                        removed++;
                    }
                }
                if (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Integer) {
                    int v = (Integer) ((LdcInsnNode) insn).cst;
                    AbstractInsnNode next = insn.getNext();
                    if ((v == 0 || v == 1) && next != null && next.getOpcode() == Opcodes.IFEQ) {
                        mn.instructions.remove(next);
                        mn.instructions.set(insn, new InsnNode(Opcodes.NOP));
                        removed++;
                    }
                }
            }
            if (removed > 0) cn.version = cn.version;
        }
    }
}
