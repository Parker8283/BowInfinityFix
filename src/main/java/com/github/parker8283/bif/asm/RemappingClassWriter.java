package com.github.parker8283.bif.asm;

import static com.github.parker8283.bif.BowInfinityFix.log;

import org.objectweb.asm.ClassWriter;

public class RemappingClassWriter extends ClassWriter {

    RemappingClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        if(!type1.startsWith("net/minecraft") || !type2.startsWith("net/minecraft")) {
            return super.getCommonSuperClass(type1, type2);
        }

        Class<?> a = null, b = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            a = Class.forName(RemappingHelper.toDeobfClassName(type1.replace('/', '.')), false, classLoader);
            b = Class.forName(RemappingHelper.toDeobfClassName(type2.replace('/', '.')), false, classLoader);
        } catch(ClassNotFoundException e) {
            log.error("This should never happen...", e);
        }

        if(a.isAssignableFrom(b)) {
            return type1;
        } else if(b.isAssignableFrom(a)) {
            return type2;
        } else if(a.isInterface() || b.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                a = a.getSuperclass();
            } while(!a.isAssignableFrom(b));
            return RemappingHelper.toObfClassName(a.getName()).replace('.', '/');
        }
    }
}
