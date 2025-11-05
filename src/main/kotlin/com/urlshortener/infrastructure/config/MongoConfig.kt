package com.urlshortener.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.urlshortener.infrastructure.repository"])
class MongoConfig