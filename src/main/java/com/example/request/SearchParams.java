package com.example.request;

import com.aerospike.client.query.Filter;
import com.example.model.BinMeta;
import com.example.model.UserMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchParams {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String MOBILE = "mobile";
    private static final List<String> validQueryParams = List.of(ID, NAME, AGE, CITY, COUNTRY, MOBILE);
    private final LinkedHashMap<String, String> params;

    public static SearchParams build(LinkedHashMap<String, String> params) {
        SearchParams searchParams = new SearchParams(params);
        searchParams.isValid();
        return searchParams;
    }

    private SearchParams(LinkedHashMap<String, String> params) {
        this.params = params;
    }

    public void isValid() {
        List<String> invalidQueryParams = this.params.keySet()
                                                     .stream()
                                                     .filter(x -> !validQueryParams.contains(x))
                                                     .toList();
        if (!invalidQueryParams.isEmpty()) {
            throw new RuntimeException("Invalid Search Parameters: " + invalidQueryParams);
        }
    }

    public boolean containsPrimary() {
        return this.params.containsKey(UserMeta.primary.searchParamName());
    }

    public Set<Map.Entry<String, String>> nameAndValues() {
        return this.params.entrySet();
    }

    public Set<Map.Entry<String, String>> nameAndValuesExcept(String name) {
        return this.params.entrySet().stream().filter(p -> !p.getKey().equals(name)).collect(Collectors.toSet());
    }

    public Set<String> names() {
        return this.params.keySet();
    }

    public String getStringValue(String name) {
        return this.params.get(name);
    }

    public Integer getIntValue(String name) {
        return Integer.valueOf(this.params.get(name));
    }

    public Long getLongValue(String name) {
        return Long.valueOf(this.params.get(name));
    }

    @Override
    public String toString() {
        return params.toString();
    }

    public String getId() {
        return this.params.get(ID);
    }

    public boolean containsSecondaryIndex() {
        return UserMeta.containsSecondaryIndexSearchParam(params.keySet());
    }

    public Filter getFilter(BinMeta secondaryIndexBin) {
        return switch (secondaryIndexBin.type()) {
            case INT -> Filter.equal(secondaryIndexBin.name(), getLongValue(secondaryIndexBin.searchParamName()));
            case STRING -> Filter.equal(secondaryIndexBin.name(), getStringValue(secondaryIndexBin.searchParamName()));
            default -> throw new IllegalStateException("Unexpected value: " + secondaryIndexBin.type());
        };
    }
}
