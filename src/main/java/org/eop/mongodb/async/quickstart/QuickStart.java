package org.eop.mongodb.async.quickstart;

import java.util.Arrays;

import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.connection.ClusterSettings;

/**
 * @author lixinjie
 * @since 2017-05-19
 */
public class QuickStart {

	public static void main(String[] args) {
		//带有设置的客户端
		ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(Arrays.asList(new ServerAddress("localhost"))).build();
		MongoClientSettings settings = MongoClientSettings.builder()
                .clusterSettings(clusterSettings).build();
		MongoClient mongoClient = MongoClients.create(settings);
		
		//插入，更新，删除，查询都是带有回调的。
		
		//可观察的，https://mongodb.github.io/mongo-java-driver/3.4/driver-async/reference/observables/
		//异步驱动是完全基于回调的，广泛使用SingleResultCallback<T>接口来进行实现，只有一个方法onResult(T result, Throwable t)
		//一旦操作完成或报错，就调用此方法。这允许用户应用逻辑被延迟直到底层到MongoDB的网络IO完成。
		//回调模式非常灵活，但是也会变得不适用，如果应用逻辑要求一个链式操纵。嵌套的回调使代码变得更难读和复杂。
		//基于此又发布了两个可观测的基于异步的驱动
		
		//构建器，https://mongodb.github.io/mongo-java-driver/3.4/builders/
		//Filters，构建查询过滤器
		//Projections，构建投影
		//Sorts，构建排序
		//Aggregation，构建聚合管道
		//Updates，构建更新
		//Indexes，构建索引
		
		
	}

}
