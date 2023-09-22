package com.czc.es_doc;

import com.czc.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * 批量新增文档
 *
 * @Author : chinzicam
 * @create 2023/9/22 20:13
 */
public class ESTest_Doc_Insert_Batch {
    public static void main(String[] args) throws IOException {
        // 创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        //创建批量新增请求对象
        BulkRequest request = new BulkRequest();
        request.add(new
                IndexRequest().index("user").id("1002").source(XContentType.JSON, "name",
                "李四"));
        request.add(new
                IndexRequest().index("user").id("1003").source(XContentType.JSON, "name",
                "王五"));
        request.add(new
                IndexRequest().index("user").id("1004").source(XContentType.JSON, "name",
                "赵六"));
        //客户端发送请求，获取响应对象
        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
        //打印结果信息
        System.out.println("took:" + responses.getTook());
        System.out.println("items:" + responses.getItems());

        // 关闭客户端连接
        client.close();

    }
}
