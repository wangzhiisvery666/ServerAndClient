package test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get("D:\\picture.jpg")));
        // The size of bytes per transfer is 1024
        byte bytes[] = new byte[1024];
        int len;
        while ((len = bis.read(bytes, 0, bytes.length)) != -1) {

        }
    }
}
