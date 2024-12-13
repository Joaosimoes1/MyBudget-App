package org.example.gps_g22.model.commands;

import java.util.Stack;

public class CommandManager {
    private final Stack<ICommand> history;
    private final Stack<ICommand> redoCmds;

    public CommandManager() {
        history =  new Stack<>();
        redoCmds = new Stack<>();
    }
    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        history.clear();
        return false;
    }
    public boolean undo() {
        if (history.isEmpty())
            return false;

        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true; }

    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;

        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }

    public boolean hasUndo() {
        return !history.isEmpty();
    }
    public boolean hasRedo() {
        return !redoCmds.isEmpty();
    }
}