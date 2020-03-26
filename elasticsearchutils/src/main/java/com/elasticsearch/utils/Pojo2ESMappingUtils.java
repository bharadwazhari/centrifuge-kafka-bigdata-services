package com.elasticsearch.utils;


import com.elasticsearch.bo.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.net.InetAddress;

public class Pojo2ESMappingUtils {

    public static void main(String args []) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(createDomainObject(Product.class));
        Client client = client();
        IndexRequestBuilder builder = client
                .prepareIndex("product_index", "_doc").setSource(json, XContentType.JSON);
        IndexResponse response = builder.execute().actionGet();
        System.out.println(response);
    }

    private static Object createDomainObject(Class classObj) {
        PodamFactory factory = new PodamFactoryImpl();
        Object obj = factory.manufacturePojoWithFullData(classObj);
        return obj;
    }

    public static Client client() throws Exception {

        Settings esSettings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();
        TransportClient client = new PreBuiltTransportClient(esSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        return client;
    }

}
