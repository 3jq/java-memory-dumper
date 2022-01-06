package dev.sl33py.jmd;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yoursleep
 * @since 30/12/21
 */
public class Main {
    public static void main(String[] args) throws Throwable {
        System.out.println("[$] java memory dumper loaded");
        System.out.println("[$] brought for you by yoursleep in 2022");
        System.out.println("[.] initializing unsafe...");

        /* read more about Unsafe.getUnsafe() if you are wondering why are we doing it this way */
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        System.out.println("[+] done");

        /* how could we dump all of the class addresses without even knowing which class are we dumping */
        System.out.println("[?] enter the class name, so the dumper could find all of the addresses: ");

        /* trying to find the class... */
        Class<?> class2dump;
        try { class2dump = Class.forName(new Scanner(System.in).nextLine()); }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("[!] class wasn't found!");
            System.out.println("[$] instructions for being able to find the class:");
            System.out.println("[$] 1. make a program for reading all of the bytes of a file");
            System.out.println("[$] 2. write all of these bytes to a variable and load this class inside the program you want to dump");
            System.out.println("[$] also make sure your jvm is 64 bit and your class exists");
            System.out.println("[$] stacktrace: ");
            throw classNotFoundException;
        }

        System.out.println("[+] class was successfully loaded!");

        /* is additional info needed? */
        System.out.println("[?] write additional info (prints things like annotations, is object synthetic and some different things) (Y/N):");
        boolean aiFlag = new Scanner(System.in).nextLine().equalsIgnoreCase("Y");

        System.out.println("\n[.] looking for addresses...");
        System.out.println("[+] dumped class pointer address: " + Long.toHexString(unsafe.getLong(class2dump, 8L)));

        System.out.println("[.] looking for fields addresses...");
        if (class2dump.getDeclaredFields().length == 0) System.out.println("there are no fields!");
        else for (Field field : class2dump.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println("[+] dumped address of a pointer to a field \"" + field.getName() + "\" with type \"" + field.getGenericType() + "\": " + Long.toHexString(unsafe.getLong(field, 8L)));
            if (aiFlag) System.out.println("[+] additional info: annotations(" + Arrays.toString(field.getDeclaredAnnotations()) + "), synthetic(" + field.isSynthetic() + ")");
        }
        System.out.println("[$] done\n");

        System.out.println("[.] looking for methods addresses...");
        if (class2dump.getDeclaredMethods().length == 0) System.out.println("[-] there are no methods!");
        else for (Method method : class2dump.getDeclaredMethods()) {
            method.setAccessible(true);
            System.out.println("[+] dumped address of a pointer to a method \"" + method.getName() + "\" with return type \"" + method.getReturnType() + "\": " + Long.toHexString(unsafe.getLong(method, 8L)));
            if (aiFlag) System.out.println("[+] additional info: parameters(" + Arrays.toString(method.getParameters()) + "), annotations(" + Arrays.toString(method.getDeclaredAnnotations()) + "), synthetic(" + method.isSynthetic() + ")");
        }
        System.out.println("[$] done");
    }


}
