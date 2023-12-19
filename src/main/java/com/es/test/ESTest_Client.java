package com.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;


public class ESTest_Client {

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
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http"))
        );

        System.out.println(esClient);
        //关闭客户端
        esClient.close();
    }
}
