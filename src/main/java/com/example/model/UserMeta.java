package com.example.model;

import com.aerospike.client.exp.Exp;
import com.example.request.SearchParams;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMeta {

    static class BinNames {
        static final String ID = "id";
        static final String NAME = "name";
        static final String AGE = "age";
        static final String CITY = "city";
        static final String COUNTRY = "country";
        static final String MOBILE = "mobile";
    }

    public static final BinMeta ID = new BinMeta(SearchParams.ID, BinNames.ID, Exp.Type.STRING, true);
    public static final BinMeta NAME = new BinMeta(SearchParams.NAME, BinNames.NAME, Exp.Type.STRING);
    public static final BinMeta CITY = new BinMeta(SearchParams.CITY, BinNames.CITY, Exp.Type.STRING, false, true);
    public static final BinMeta COUNTRY = new BinMeta(SearchParams.COUNTRY, BinNames.COUNTRY, Exp.Type.STRING);
    public static final BinMeta MOBILE = new BinMeta(SearchParams.MOBILE, BinNames.MOBILE, Exp.Type.STRING);
    public static final BinMeta AGE = new BinMeta(SearchParams.AGE, BinNames.AGE, Exp.Type.INT, false, true);

    public static final LinkedList<BinMeta> bins = new LinkedList<>() {{
        add(ID);
        add(NAME);
        add(CITY);
        add(COUNTRY);
        add(MOBILE);
        add(AGE);
    }};


    private static final Map<String, BinMeta> searchParamToBins = bins.stream()
                                                                      .collect(Collectors.toMap(BinMeta::searchParamName,
                                                                                                x -> x,
                                                                                                (x, y) -> y,
                                                                                                LinkedHashMap::new));

    public static BinMeta primary = bins.stream()
                                        .filter(BinMeta::isPrimary)
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Primary Index not found"));


    public static final Map<String, BinMeta> searchParamsToSecondaryIndexBins =
            searchParamToBins.entrySet()
                             .stream()
                             .filter(e -> e.getValue().isSecondary())
                             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public static boolean containsSecondaryIndexSearchParam(Set<String> searchParamNames) {
        return searchParamsToSecondaryIndexBins.keySet().stream().anyMatch(searchParamNames::contains);
    }

    public static BinMeta getBinMeta(String searchParamName) {
        return searchParamToBins.get(searchParamName);
    }

    public static Optional<BinMeta> getSecondaryIndexBin(Set<String> searchParamNames) {
        return searchParamsToSecondaryIndexBins.entrySet()
                                               .stream()
                                               .filter(e -> searchParamNames.contains(e.getKey()))
                                               .findFirst()
                                               .map(Map.Entry::getValue);
    }
}
