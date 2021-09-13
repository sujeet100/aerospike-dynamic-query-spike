package com.example.repository;

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.mapper.tools.AeroMapper;
import com.example.model.BinMeta;
import com.example.model.User;
import com.example.model.UserMeta;
import com.example.request.SearchParams;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import static com.aerospike.client.exp.Exp.eq;
import static com.aerospike.client.exp.Exp.val;
import static com.example.model.UserMeta.getBinMeta;

@Singleton
public class UserRepository {
    @Inject
    AeroMapper aeroMapper;


    public User getById(SearchParams searchParams) {
        QueryPolicy queryPolicy = getQueryPolicyWithExpressions(searchParams, SearchParams.ID);

        return aeroMapper.read(queryPolicy, User.class, searchParams.getId());
    }

    public List<User> getByFilterAndExpression(SearchParams searchParams) {
        BinMeta secondaryIndexBin = UserMeta.getSecondaryIndexBin(searchParams.names())
                                            .orElseThrow(() -> new RuntimeException(
                                                    "No bin with secondary index found"));
        Filter filter = searchParams.getFilter(secondaryIndexBin);

        QueryPolicy queryPolicy = getQueryPolicyWithExpressions(searchParams, secondaryIndexBin.searchParamName());

        List<User> users = new ArrayList<>();
        aeroMapper.query(queryPolicy, User.class, x -> {
            users.add(x);
            return true;
        }, filter);
        return users;
    }

    private QueryPolicy getQueryPolicyWithExpressions(SearchParams searchParams, String excludeParam) {
        Exp[] clauses = searchParams.nameAndValuesExcept(excludeParam)
                                    .stream()
                                    .map(e -> {
                                        BinMeta binMeta = getBinMeta(e.getKey());
                                        return eq(Exp.bin(binMeta.name(), binMeta.type()), val(e.getValue()));
                                    }).toArray(Exp[]::new);
        if (clauses.length == 0) {
            return new QueryPolicy();
        }

        Expression exp = Exp.build(clauses.length == 1 ? clauses[0] : Exp.and(clauses));

        QueryPolicy queryPolicy = new QueryPolicy();
        queryPolicy.filterExp = exp;
        return queryPolicy;
    }

    public void save() {
        aeroMapper.save(new User("1", "Sujit", 33, "Pune", "IN", "7777777777"));
        aeroMapper.save(new User("2", "Kamthe", 22, "Mumbai", "US", "8888888888"));
        aeroMapper.save(new User("3", "Foo", 42, "Mumbai", "IN", "8888888888"));
        aeroMapper.getClient().createIndex(null, "test", "users", "age", "age", IndexType.NUMERIC);
        aeroMapper.getClient().createIndex(null, "test", "users", "city", "city", IndexType.NUMERIC);
    }
}
