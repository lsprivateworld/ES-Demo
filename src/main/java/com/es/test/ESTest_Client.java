package com.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class ESTest_Client {

    private static  RestHighLevelClient esClient;
    /**
     * 倒排索引：
     * 正排索引：是以文档对象的唯一 ID 作为索引，以文档内容作为记录的结构。
     * 倒排索引：Inverted index，指的是将文档内容中的单词作为索引，将包含该词的文档 ID 作为记录的结构。
     * https://blog.csdn.net/meser88/article/details/131135522
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception{

        //创建ES客户端
        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http"))
        );


        /**
         * PUT person
         * {
         *   "settings": {
         *     "number_of_shards": 3,
         *     "number_of_replicas: 1
         *   },
         *   "mappings": {
         *   "properties": {
         *     "name": {
         *      "type": "text"
         *     },
         *     "age": {
         *         "type": "integer"
         *       },
         *     "birthday": {
         *         "type": "date",
         *         "format": "yyyy-MM-dd"
         *     }
         *   }
         * }
         */

        System.out.println(esClient);
        //关闭客户端
        esClient.close();
    }

    //创建索引
    @Test
    static void createIndex() throws IOException {
        //1、设置setting对象
        Settings settings = Settings.builder()
                //分片数
                .put("number_of_shards", 5)
                //副本数
                .put("number_of_replicas", 1)
                .build();
        //2、设置mappings对象
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type", "text")
                .endObject()
                .endObject()
                .endObject();
        //3、创建索引的请求对象
        CreateIndexRequest request = new CreateIndexRequest("person").settings(settings).mapping(mappings);

        //4、使用client客户端发送创建索引的请求
        //通过client 对象把上面准备的 request 对象发送到es执行
        CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);

        //5、获取响应对象
        System.out.println(response);
    }

    //检查索引是否存在，删除索引
    @Test
    public void testExist() throws IOException {
        //1、准备request对象
        String index = "person";
        GetIndexRequest request = new GetIndexRequest(index);
        //2、通过client去操作
        boolean exists = esClient.indices().exists(request, RequestOptions.DEFAULT);
        //3、输出
        System.out.println(exists);

        //删除
        DeleteIndexRequest request1 = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = esClient.indices().delete(request1, RequestOptions.DEFAULT);

        System.out.println("是否删除成功" + delete.isAcknowledged());
    }

}
