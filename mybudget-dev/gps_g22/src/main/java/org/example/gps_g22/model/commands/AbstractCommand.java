package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.DataModel;

public abstract class AbstractCommand implements ICommand {
    protected final DataModel model;

    protected AbstractCommand(DataModel model) {
        this.model = model;
    }

}
