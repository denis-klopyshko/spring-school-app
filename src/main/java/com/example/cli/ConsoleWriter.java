package com.example.cli;

import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleWriter {
    private final Terminal terminal;

    public void printSuccess(String message) {
        print(message, 2);
    }

    public void printInfo(String message) {
        print(message, 6);
    }

    public void printWarning(String message) {
        print(message, 3);
    }

    public void printError(String message) {
        print(message, 2);
    }

    public void print(String message, int color) {
        String toPrint = getColored(message, color);
        terminal.writer().println(toPrint);
        terminal.flush();
    }

    public void print(String message) {
        terminal.writer().println(message);
        terminal.flush();
    }

    private String getColored(String message, int color) {
        return (new AttributedStringBuilder()).append(message, AttributedStyle.DEFAULT.foreground(color)).toAnsi();
    }
}
