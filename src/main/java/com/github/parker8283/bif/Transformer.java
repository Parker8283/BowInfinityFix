package com.github.parker8283.bif;

import static org.objectweb.asm.Opcodes.*;
import static com.github.parker8283.bif.BowInfinityFix.log;

import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

import com.github.parker8283.bif.asm.ASMHelper;
import com.github.parker8283.bif.asm.RemappingHelper;

public class Transformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.item.ItemBow")) {
            log.info("Found ItemBow class");

            boolean isObfuscated = !name.equals(transformedName);

            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? "a" : "onItemRightClick", "(" + RemappingHelper.getDescriptor("net.minecraft.item.ItemStack") + RemappingHelper.getDescriptor("net.minecraft.world.World") + RemappingHelper.getDescriptor("net.minecraft.entity.player.EntityPlayer") + ")" + RemappingHelper.getDescriptor("net.minecraft.item.ItemStack"));
            if(methodNode != null) {
                log.info("Found onItemRightClick method");
                injectInfinityCheck(methodNode, isObfuscated);
            } else {
                throw new RuntimeException("Could not find onItemRightClick method in ItemBow class");
            }

            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }

    private void injectInfinityCheck(MethodNode method, boolean isObfuscated) {
        /* Injected Bytecode
        GETSTATIC net/minecraft/enchantment/Enchantment.infinity : Lnet/minecraft/enchantment/Enchantment;
        GETFIELD net/minecraft/enchantment/Enchantment.effectId : I
        ALOAD 1
        INVOKESTATIC net/minecraft/enchantment/EnchantmentHelper.getEnchantmentLevel (ILnet/minecraft/item/ItemStack;)I
        IFGT L5
         */
        AbstractInsnNode targetNode = ASMHelper.getOrFindInstructionOfType(method.instructions.getFirst(), IFNE, 1, false);

        InsnList toInject = new InsnList();
        toInject.add(new FieldInsnNode(GETSTATIC, RemappingHelper.getInternalClassName("net.minecraft.enchantment.Enchantment"), isObfuscated ? "y" : "infinity", RemappingHelper.getDescriptor("net.minecraft.enchantment.Enchantment")));
        toInject.add(new FieldInsnNode(GETFIELD, RemappingHelper.getInternalClassName("net.minecraft.enchantment.Enchantment"), isObfuscated ? "B" : "effectId", "I"));
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new MethodInsnNode(INVOKESTATIC, RemappingHelper.getInternalClassName("net.minecraft.enchantment.EnchantmentHelper"), isObfuscated ? "a" : "getEnchantmentLevel", "(I" + RemappingHelper.getDescriptor("net.minecraft.item.ItemStack") + ")I"));
        toInject.add(new JumpInsnNode(IFGT, ((JumpInsnNode)targetNode).label));

        method.instructions.insertBefore(targetNode.getNext(), toInject);

        log.info("Fix injected! Enjoy!");
    }
}
