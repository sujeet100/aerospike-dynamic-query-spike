package com.example.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("aerospike")
public record AerospikeProperties(String hostname, Integer port, String namespace) {
}

