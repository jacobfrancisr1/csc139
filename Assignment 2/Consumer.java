/*
 * Consumer.java
 * Author: Jacob Francis
 * Date: 11/22/2025
 *
 * Description:
 * This program acts as the consumer in a shared-memory IPC setup.
 * It opens the shared memory file created by Producer, maps it into
 * memory using FileChannel + MappedByteBuffer, and reads the characters
 * from a CharBuffer until it hits the null terminator written by Producer.
 * It then prints the received message.
 */

import java.io.*;
import java.util.*;
import java.nio.channels.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-I4q6KstA$ 
import java.nio.file.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/file/StandardOpenOption.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-LCIR2laQ$ 
import java.nio.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/MappedByteBuffer.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-JYoe5idQ$ 
import java.nio.channels.FileChannel.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.MapMode.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-IJ7GhVvw$ 

public class Consumer {

    public static final String shared_name = "OS";
    public static final int SIZE = 512;

    public static void main(String[] args) throws java.io.IOException {
        File shmem = new File(shared_name);

        // (1) Open FileChannel with READ permission
        FileChannel fc = FileChannel.open(
                shmem.toPath(),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE // Allow reading the mapped region fully
        );

        // (2) Map the file into memory
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, SIZE);

        // (3) Wrap with CharBuffer so we can read chars
        CharBuffer cb = mbb.asCharBuffer();

        // Read until null terminator '\0'
        StringBuilder output = new StringBuilder();
        char c;

        while ((c = cb.get()) != '\0') {
            output.append(c);
        }

        System.out.println("Consumer read: " + output.toString());

        fc.close();
    }
}
