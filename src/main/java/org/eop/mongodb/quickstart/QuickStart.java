package org.eop.mongodb.quickstart;

import java.util.Arrays;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

/**
 * 
 * @author lixinjie
 * @since 2017-05-19
 */
public class QuickStart {

	public static void main(String[] args) {
		//代表一个连接池，多线程时也只需一个实例
		MongoClient mc = new MongoClient("localhost", 27017);
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
		mc = new MongoClient(connectionString);
		//访问数据库，当不存在时，首次往里存储数据时会被自动创建，数据库实例是不可变的
		MongoDatabase mdb = mc.getDatabase("mydb");
		//创建文档
		Document doc = new Document();
		doc.append("name", "MongoDB")
		   .append("type", "database")
		   .append("count", 1)
		   .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
		   .append("", new Document("x", 203).append("y", 102));
		//创建集合
		mdb.createCollection("mycoll");
		MongoCollection<Document> coll = mdb.getCollection("mycoll");
		//插入文档，如果没有指定_id字段，数据库会自动生成
		coll.insertOne(doc);
		coll.insertMany(Arrays.asList(new Document(), new Document(), new Document()));
		//获取集合里文档数量
		coll.count();
		//查询集合第一个元素
		coll.find().first();
		//查询所有文档
		coll.find().iterator();
		//使用过滤器
		coll.find(Filters.or(Filters.eq("name", "MongoDB"), Filters.ne("version", "v3.2")));
		//更新文档，指定一个空的文档作为条件将匹配所有
		coll.updateOne(Filters.eq("name", "MongoDB"), new Document("count", 100));
		coll.updateMany(new Document(), new Document("version", "v3.4"));
		//删除文档
		coll.deleteOne(Filters.eq("name", "MongoDB"));
		coll.deleteMany(Filters.gte("count", 0));
		//创建索引，升序为1降序为-1
		coll.createIndex(new Document("name", 1));
		coll.createIndex(new Document("name", -1));
		//**********************************************************************************************//
		//连接到Mong哦DB，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/connect-to-mongodb/
		//设置连接选项的两种方式
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1&ssl=true"));
		String user = ""; // the user name
		String database = ""; // the name of the database in which the user is defined
		char[] password = new char[10]; // the password as a character array
		
		MongoCredential credential = MongoCredential.createCredential(user, database, password);
		MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
		MongoClient mongoClient1 = new MongoClient(new ServerAddress("host1", 27017),
		                                   Arrays.asList(credential),
				                                           options);
		//启用SSL，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/ssl/
		
		//几种授权机制，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/authentication/
		
		//JNDI，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/jndi/
		
		//数据库和集合，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/databases-collections/
		//集合不存在时，首次往里存储数据时会被创建，或者可以显式创建集合，集合是不可变的
		
		//创建索引，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/indexes/
		//升序索引，降序索引，复合索引，文本索引（支持字符串内容的文本搜索），哈希索引，地理位置索引，可以指定索引选项（唯一索引，部分索引）
		
		//读操作，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/perform-read-operations/
		//可以使用投影（projection）来指定返回的字段，而不是返回所有字段。可以进行排序。可以在客户端、数据库或集合这个三个级别设置读偏好。也可在这三个级别设置读关注。
		
		//写作操，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/perform-write-operations/
		//插入，更新，替换，删除。可以从和上面一样的三个级别配置写关注。
		coll.updateOne(Filters.eq("_id", new ObjectId("57506d62f57802807471dd41")), Updates.combine(Updates.set("stars", 1), Updates.set("contact.phone", "228-555-9999"), Updates.currentDate("lastModified")));
		coll.updateMany(Filters.lt("_id", new ObjectId("57506d62f57802807471dd41")), Updates.combine(Updates.set("stars", 1), Updates.set("contact.phone", "228-555-9999"), Updates.currentDate("lastModified")));
		//替换时文档结构不用和原来的一样，且不用指定_id字段，如果指定，则必须和被替换的一样，因为_id字段是不可变的，不能被更新
		coll.replaceOne(
				Filters.eq("_id", new ObjectId("57506d62f57802807471dd41")),
                new Document("name", "Green Salads Buffet")
                        .append("contact", "TBD")
                        .append("categories", Arrays.asList("Salads", "Health Foods", "Buffet")));
		
		coll.deleteOne(Filters.eq("_id", new ObjectId("57506d62f57802807471dd41")));
		coll.deleteMany(Filters.eq("stars", 4));
		
		//批量操作，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/bulk-writes/
		//插入，更新，替换，删除可以同时放入一个批量操作里面执行，分为有序批量和无序批量，有序批量按顺序执行，遇到第一个错误就停止，无序批量随意执行，最后报告错误。
		
		//聚合框架，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/aggregation/
		//聚合管道是一个用于数据聚合的框架，仿效数据处理管道的概念
		//先按categories字段进行过滤，然后按stars字段进行分组，同时累积分组的个数
		coll.aggregate(
			      Arrays.asList(
			              Aggregates.match(Filters.eq("categories", "Bakery")),
			              Aggregates.group("$stars", Accumulators.sum("count", 1))
			      )
			);
		
		//文本搜索，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/text-search/
		//基于文本索引，在字符串内容上执行文本搜索
		//在name字段上搜索bakery或coffee
		coll.createIndex(Indexes.text("name"));
		coll.count(Filters.text("bakery coffee"));
		//文本分数，对于每个匹配的文档会赋一个值，表示文档和搜索过滤器的相关性程度，可以按分数排序并返回
		coll.find(Filters.text("bakery cafe"))
        .projection(Projections.metaTextScore("score"))
        .sort(Sorts.metaTextScore("score"));
		
		//地理位置搜索，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/geospatial-search/
		//基于地理位置索引，2d球面索引，在某点附近搜索位置，从近到远排序
		coll.createIndex(Indexes.geo2dsphere("contact.location"));
		Point refPoint = new Point(new Position(-73.9667, 40.78));
		coll.find(Filters.near("contact.location", refPoint, 5000.0, 1000.0));
		
		//网格文件存储，https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/gridfs/
		//GridFS是一个规范用于存储和获取超过BSON文档16M大小限制的文件。把文件分成部分或块，每部分或块单独存储成一个文档，当获取文件时这些块可以重新组合。
		//包括文件的上传、查询、下载，重命名，删除。
	}

}
