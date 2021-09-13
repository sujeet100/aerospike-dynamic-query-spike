package com.example.model;

import com.aerospike.client.exp.Exp;

public record BinMeta(
        String searchParamName,
        String name,
        Exp.Type type,
        boolean isPrimary,
        boolean isSecondary
) {

    public BinMeta(String searchParamName, String name, Exp.Type type) {
        this(searchParamName, name, type, false, false);
    }

    public BinMeta(String searchParamName, String name, Exp.Type type, boolean isPrimary) {
        this(searchParamName, name, type, isPrimary, false);
    }
}
