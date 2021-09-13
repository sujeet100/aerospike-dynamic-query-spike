package com.example.config;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.mapper.tools.AeroMapper;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;


@Factory
public class AerospikeConfig {
    @Singleton
    public AerospikeClient aerospikeClient(AerospikeProperties aerospikeProperties) {
        return new AerospikeClient(aerospikeProperties.hostname(), aerospikeProperties.port());
    }

    @Singleton
    public WritePolicy aerospikeWritePolicy() {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.setTimeout(10000);
        return writePolicy;
    }

    @Singleton
    public AeroMapper aerospikeMapper(AerospikeClient aerospikeClient) {
        return new AeroMapper.Builder(aerospikeClient).build();
    }
}
