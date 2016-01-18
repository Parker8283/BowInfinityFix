package com.github.parker8283.bif;

import static org.objectweb.asm.Opcodes.*;
import static com.github.parker8283.bif.BIFLoader.log;

import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

import com.github.parker8283.bif.asm.ASMHelper;

public class Transformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.item.ItemBow")) {
            log.info("Found ItemBow class");

            boolean isDev = !BIFLoader.DEV_ENV;

            ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isDev ? "onItemRightClick" : "func_77659_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;");
            if(methodNode != null) {
                log.info("Found onItemRightClick method");
                injectInfinityCheck(methodNode, isDev);
            } else {
                throw new RuntimeException("Could not find onItemRightClick method in ItemBow class");
            }

            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }

    private void injectInfinityCheck(MethodNode method, boolean isDev) {
        /* Injected Bytecode
        GETSTATIC net/minecraft/enchantment/Enchantment.infinity : Lnet/minecraft/enchantment/Enchantment;
        GETFIELD net/minecraft/enchantment/Enchantment.effectId : I
        ALOAD 1
        INVOKESTATIC net/minecraft/enchantment/EnchantmentHelper.getEnchantmentLevel (ILnet/minecraft/item/ItemStack;)I
        IFGT L5
         */
        AbstractInsnNode targetNode = ASMHelper.getOrFindInstructionOfType(method.instructions.getFirst(), IFNE, 1, false);

        InsnList toInject = new InsnList();
        toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/enchantment/Enchantment", isDev ? "infinity" : "field_77342_w", "Lnet/minecraft/enchantment/Enchantment;"));
        toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/enchantment/Enchantment", isDev ? "effectId" : "field_77352_x", "I"));
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/enchantment/EnchantmentHelper", isDev ? "getEnchantmentLevel" : "func_77506_a", "(ILnet/minecraft/item/ItemStack;)I", false));
        toInject.add(new JumpInsnNode(IFGT, ((JumpInsnNode)targetNode).label));

        method.instructions.insertBefore(targetNode.getNext(), toInject);

        log.info("Fix injected! Enjoy!");
    }
}
