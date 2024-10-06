package ru.MaslovArtemy.NauJava.output;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
public class OutputHandler implements Printer {
    private final OutputStream outputStream = System.out;

    @SneakyThrows
    public void print(String string) {
        outputStream.write(string.getBytes());
    }
}
