package com.imooc.files;




import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
public class GridFSConfig {
    @Value("${spring.data.mongodb.database}")
    private String mongodb;

    public GridFSConfig() {}

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(mongodb);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);

        return gridFSBucket;
    }
}