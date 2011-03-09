/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.tools.punj;

import com.sun.tools.javac.jvm.ClassReader;

import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 3/9/11
 * Time: 6:27 PM
 * Package: ch.idsia.tools.punj
 */
public class PunctualJudge
{
private static int counter;
protected Hashtable<String, Class> cache = new Hashtable<String, Class>();

public static void main(String[] args) throws IllegalAccessException, InstantiationException
{
    System.out.println("PunJ-0.0.1");
    PunctualJudge punctualJudge = new PunctualJudge();
    // Before Instrumentation
    final String classFileName = /*args[0];*/ "bin/ch/idsia/punj/DemoClass.class";
    try
    {
        // create a ClassReader that loads the Java .class file specified as the command line argument
        System.out.println("Disassembling " + classFileName + " Before Instrumentation:");
        final ClassReader cr = new ClassReader(new FileInputStream(classFileName));
        // create an empty ClassNode (in-memory representation of a class)
        final ClassNode clazz = new ClassNode();
        // have the ClassReader read the class file and populate the ClassNode with the corresponding information
        cr.accept(clazz, 0);
        // create a dumper and have it dump the given ClassNode
//            final JavaClassDisassembler dumper = new JavaClassDisassembler();
//            dumper.disassembleClass(clazz);
    } catch (Exception e)
    {
        e.printStackTrace();
    }
//        DemoClass demoClass = new DemoClass();
//        int sum = demoClass.forWithBreakMethod(42);
//        System.out.println("sum = " + sum);
//        System.out.println("punctualJudge = " + DemoClass.getCounter());

    // After Instrumentation
    // create a ClassReader that loads the Java .class file specified as the command line argument

    byte[] data;
    try
    {
        System.out.println("\n\nDisassembling " + classFileName + " After Instrumentation:");
        data = punctualJudge.instrumentClass(classFileName);
        System.out.println("data = " + data);
        final ClassReader cr = new ClassReader(data);
        // create an empty ClassNode (in-memory representation of a class)
        final ClassNode clazz = new ClassNode();
        // have the ClassReader read the class file and populate the ClassNode with the corresponding information
        cr.accept(clazz, 0);
        // create a dumper and have it dump the given ClassNode
//            final JavaClassDisassembler dumper = new JavaClassDisassembler();
//            dumper.disassembleClass(clazz);

        final String loadClassFileName = classFileName.replace("bin/", "").replaceAll(".class", "").replace("/", ".");
        System.out.println("loadClassFileName = " + loadClassFileName);
        Class c = punctualJudge.defineClass(loadClassFileName, data, 0, data.length);
        Demo demo = (Demo) c.newInstance();
        int sum = demo.forWithBreakMethod(11);
        System.out.println("\ndemo = " + sum);
        System.out.println("\npunctualJudge = " + demo.getCounter());
    } catch (final IOException e)
    {
        e.printStackTrace();
    }

}

private byte[] getClassData(String name) throws IOException
{
    byte[] b = null;
    try
    {
//            final String pathname = "plugin-bin/ch/unisi/inf/sp/statistic/Min.class";
//            final String loadName = directory + name.replace(".", "/") + ".class";
        final String loadName = name;
//            if (!pathname.equals(loadName))
//                System.err.println("Achtung! \n" + pathname + "!=\n" + loadName);
        File file = new File(loadName);
//            System.out.println("uri.getScheme() = " + uri.());

        System.out.println("System.getProperty(\"user.dir\") = " + System.getProperty("user.dir"));
        System.out.println(name + " exists = " + file.exists());
        b = getBytesFromFile(file);
        final FileReader fileReader = new FileReader(name);

    } catch (FileNotFoundException e)
    {
        System.err.println("No class file found: " + name);
    }

    return b;
}


public static byte[] getBytesFromFile(File file) throws IOException
{
    InputStream is = new FileInputStream(file);

    // Get the size of the file
    long length = file.length();
    System.out.println("length = " + length);

    if (length > Integer.MAX_VALUE)
    {
        // File is too large
    }

    // Create the byte array to hold the data
    byte[] bytes = new byte[(int) length];

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
            && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
    {
        offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length)
    {
        throw new IOException("Could not completely read file " + file.getName());
    }

    // Close the input stream and return bytes
    is.close();
    return bytes;
}

public static void printOut()
{
    System.out.println("Injection Invoked! " + counter);
}


public static void incrementCounter()
{
    ++counter;
    printOut();
}

private byte[] instrumentClass(final String classFileName) throws IOException
{
    byte[] initialClass = getClassData(classFileName);
    byte[] instrumentedClass = null;
    final ClassReader cr = new ClassReader(new FileInputStream(classFileName));
    // create an empty ClassNode (in-memory representation of a class)
    final ClassNode clazz = new ClassNode();
    // have the ClassReader read the class file and populate the ClassNode with the corresponding information
    cr.accept(clazz, 0);

    System.out.println("Instrumenting Class: " + clazz.name);
    // get the list of all methods in that class
    final List<MethodNode> methods = clazz.methods;
    for (int m = 0; m < methods.size(); m++)
    {
        final MethodNode method = methods.get(m);
        if (method.name.startsWith("increment") || method.name.startsWith("getCounter"))
            continue;
        instrumentMethod(method, System.out);
    }

    ClassWriter cw = new ClassWriter(0);

//        ClassAdapter ca = new ClassAdapter(cw)
//        {
//            public MethodVisitor visitMethod(int access, String name,
//                                             String desc, String signature, String[] exceptions)
//            {
//                final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//                MethodNode mn = new MethodNode(access, name, desc, signature, exceptions)
//                {
//                    public void visitFieldInsn()
//                    {
//
//                    }
//                    public void visitEnd()
//                    {
//                        // transform or analyze method code using tree API
//                        accept(mv);
//                    }
//                };
//                return mv;
//            }
//        };
//        cr.accept(ca, 0);
    clazz.accept(cw);

    instrumentedClass = cw.toByteArray();
    return instrumentedClass;
}

private void instrumentMethod(MethodNode methodNode, PrintStream outStream)
{
    System.out.println("  Instrumenting Method: " + methodNode.name + methodNode.desc);
    // get the list of all instructions in that method
    final InsnList instructions = methodNode.instructions;
    ListIterator it = instructions.iterator();
    while (it.hasNext())
    {
//            it.add(new VarInsnNode(Opcodes.INVOKESTATIC, 0));
        final AbstractInsnNode instruction = (AbstractInsnNode) it.next();
        if (instruction.getOpcode() != -1 &&
                instruction.getOpcode() != Opcodes.RETURN &&
                instruction.getOpcode() != Opcodes.IRETURN &&
                instruction.getOpcode() != Opcodes.ARETURN)
            instructions.insert(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC, "ch/idsia/punj/DemoClass", "incrementCounter", "()V"));
        System.out.print(methodNode.instructions.size() + ",");
    }
}

public int getCounter()
{
    return counter;
}

//public synchronized Class loadClass(String fullName, boolean resolve) throws ClassNotFoundException
//{
//    if (fullName.startsWith("java."))
//    {
//        System.out.println("loadClass: SystemLoading " + fullName);
//        return findSystemClass(fullName);
//    }
//
//    Class c = cache.get(fullName);
//
//    if (c == null)
//    {
//        System.out.println("loadClass: About to genClassData " + fullName);
//        try
//        {
//            final ClassReader cr = new ClassReader(new FileInputStream(fullName));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        byte mydata[] = null;
//        System.out.println("loadClass: About to defineClass " + fullName);
//        c = defineClass(fullName, mydata, 0, mydata.length);
//        System.out.println("loadClass: storing " + fullName + " in cache.");
//        cache.put(fullName, c);
//    } else
//        System.out.println("loadClass: found " + fullName + " in cache.");
//    if (resolve)
//    {
//        System.out.println("loadClass: About to resolveClass " + fullName);
//        resolveClass(c);
//    }
//    return c;
//}
}
