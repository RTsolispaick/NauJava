package ru.MaslovArtemy.NauJava.ui.command;

public interface Command {
    String getName();
    void execute(String[] args);
}
