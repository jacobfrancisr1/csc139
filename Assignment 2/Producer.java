
/*
 * Producer.java
 * Author: Jacob Francis
 * Date: 11/22/2025
 *
 * Description:
 * This program acts as the producer in a shared-memory IPC setup.
 * It creates (or opens) a shared memory file, maps it into memory using
 * FileChannel + MappedByteBuffer, and writes a character message into it
 * through a CharBuffer. The Consumer program then reads the same data
 * using its own mapped view of the shared file.
 */

import java.io.*;
import java.util.*;
import java.nio.channels.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-I4q6KstA$ 
import java.nio.file.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/file/StandardOpenOption.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-LCIR2laQ$ 
import java.nio.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/MappedByteBuffer.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-JYoe5idQ$ 
import java.nio.channels.FileChannel.*; // https://urldefense.com/v3/__https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.MapMode.html__;!!P7nkOOY!6EdBNGuFrWQk4efZG6TYbPmS3wu_NhDQymO-LdpY4fdUwAewGhUb2tc-_-IJ7GhVvw$ 

public class Producer {

  public static final String shared_name = "OS";
  public static final int SIZE = 512;
  public static char[] message = "Hello World!\n\0".toCharArray();

  public static void main(String[] args) throws java.io.IOException {
    File shmem = new File(shared_name);

    // (1) Open FileChannel with READ, WRITE, and CREATE permissions
    FileChannel fc = FileChannel.open(
        shmem.toPath(),
        StandardOpenOption.CREATE,
        StandardOpenOption.READ,
        StandardOpenOption.WRITE);

    // (2) Map SIZE bytes of memory for READ/WRITE
    MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, SIZE);

    // (3) Use CharBuffer to write characters into shared memory
    CharBuffer cb = mbb.asCharBuffer();

    // Write the whole message
    for (char c : message) {
      cb.put(c);
    }

    System.out.println("Producer wrote message to shared memory.");

    fc.close();
  }
}
