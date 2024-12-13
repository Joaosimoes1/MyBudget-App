package org.example.gps_g22.model.commands;

public interface ICommand {
    boolean execute();
    boolean undo();
}

