package com.github.parker8283.bif.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * A Helper for ASM-related stuff. Code stolen from squeek502.
 */
public class ASMHelper {

    public static ClassNode readClassFromBytes(byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    public static byte[] writeClassToBytes(ClassNode classNode)
    {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static MethodNode findMethodNodeOfClass(ClassNode classNode, String methodName, String methodDesc)
    {
        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(methodName) && (methodDesc == null || method.desc.equals(methodDesc)))
            {
                return method;
            }
        }
        return null;
    }

    public static AbstractInsnNode getOrFindInstructionOfType(AbstractInsnNode firstInsnToCheck, int opcode, int times, boolean reverseDirection)
    {
        int timesFound = 0;
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext())
        {
            if (instruction.getOpcode() == opcode) {
                ++timesFound;
                if(times == timesFound) {
                    return instruction;
                }
            }
        }
        return null;
    }
}
