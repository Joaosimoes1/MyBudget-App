package org.example.gps_g22.model.data.transaction;

import java.io.Serial;
import java.io.Serializable;

public class TransactionSource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private String description;
    private final long nif;

    private final TransactionType type;

    public TransactionSource(String name, long nif, TransactionType type) {
        this.name = name;
        this.nif = nif;
        this.type = type;
    }

    public TransactionSource(String name, long nif, TransactionType type, String description) {
        this(name, nif, type);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getNif() {
        return nif;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
