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

            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isDev ? "onItemRightClick" : "func_77659_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;");
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
        LDC "infinity"
        INVOKESTATIC net/minecraft/enchantment/Enchantment.getEnchantmentByLocation (Ljava/lang/String;)Lnet/minecraft/enchantment/Enchantment;
        ALOAD 1
        INVOKESTATIC net/minecraft/enchantment/EnchantmentHelper.getEnchantmentLevel (Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I
        IFNE L6
         */
        AbstractInsnNode targetNode = ASMHelper.getOrFindInstructionOfType(method.instructions.getFirst(), IFNE, 2, false);

        InsnList toInject = new InsnList();
        toInject.add(new LdcInsnNode("infinity"));
        toInject.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/enchantment/Enchantment", isDev ? "getEnchantmentByLocation" : "func_180305_b", "(Ljava/lang/String;)Lnet/minecraft/enchantment/Enchantment;", false));
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/enchantment/EnchantmentHelper", isDev ? "getEnchantmentLevel" : "func_77506_a", "(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I", false));
        toInject.add(new JumpInsnNode(IFNE, ((JumpInsnNode)targetNode).label));

        method.instructions.insertBefore(targetNode.getNext(), toInject);

        log.info("Fix injected! Enjoy!");
    }
}
