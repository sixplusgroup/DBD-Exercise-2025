import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class GenerateData {
    public static void main(String[] args) {
        generatrDataIntoStudentDB();
        generatrDataIntoLibraryDB();
    }

    /**
     * 为student数据库插入数据。只有两个字段，姓名和学号。_id是自动生成的类似mysql的自增主键
     * 这个集合存在student_info集合中，集合的概念与mysql中的表类似
     * 姓名从“student1”一直到“student999999”，学号对应从1到999999。
     * 其数据形式如下：
     * {
     *         "_id" : ObjectId("5de11da02ba323694a3c5e38"),
     *         "name" : "student0",
     *         "student_id" : 0
     * }
     * {
     *         "_id" : ObjectId("5de11da02ba323694a3c5e39"),
     *         "name" : "student1",
     *         "student_id" : 1
     * }
     * {
     *         "_id" : ObjectId("5de11da02ba323694a3c5e3a"),
     *         "name" : "student2",
     *         "student_id" : 2
     * }
     * ......
     * ......
     * {
     *         "_id" : ObjectId("5de11e3c2ba323694a4ba077"),
     *         "name" : "student999999",
     *         "student_id" : 999999
     * }
     */

    public static void generatrDataIntoStudentDB(){
        /* 建立连接*/
        ServerAddress s=new ServerAddress("localhost",27017);

        /* 创建客户端*/
        MongoClient mc=new MongoClient(s);

        /* 防止已经有这个数据库，先把它删掉*/
        mc.dropDatabase("student");

        /* 连接到student数据库，如果没有这个数据库，会自动创建*/
        MongoDatabase md=mc.getDatabase("student");

        /*连接到student_info集合 */
        MongoCollection<Document> studentCollection = md.getCollection("student_info");

        /*开始时间*/
        long startMili1=System.currentTimeMillis();

        /*创建文档，并且逐个插入*/
        for (int i = 0; i < 1000000; i++) {
            Document d=new Document();
            d.append("name","student"+i);
            d.append("student_id",i);
            studentCollection.insertOne(d);
        }

        /*结束时间*/
        long endMili1=System.currentTimeMillis();


        System.out.println("生成student数据完毕，总耗时为："+(endMili1-startMili1)+"毫秒");


        Bson index= new BasicDBObject().append("student_id", 1);
        studentCollection.createIndex(index);

        System.out.println("在student_info集合的student_id上建立索引完毕！");
    }

    /**
     * 创建library数据库
     * 集合author存储作者信息。
     * 集合book1存储书的信息，采用内嵌式设计，直接把author的全部信息放在一个数组里面
     * 集合book2存储书的信息，采用引用式设计，把author的ObjectId放在数组里面
     *
     * 假设所有的书都是相同的两位作者：author123456、author654321（如果不这么做的话，这个插入数据程序可能要跑很久很久）
     *  这是author集合数据格式
     * {
     *         "_id" : ObjectId("5de11a294059586520401c37"),
     *         "name" : "author0",
     *         "country" : "China"
     * }
     * {
     *         "_id" : ObjectId("5de11a294059586520401c38"),
     *         "name" : "author1",
     *         "country" : "China"
     * }
     * ......
     * ......
     * {
     *         "_id" : ObjectId("5de11abd40595865204f5e76"),
     *         "name" : "author999999",
     *         "country" : "China"
     * }
     *
     *
     *
     *这是book1集合，里面存放1000000本书，书名从author0到author999999，作者全都是author123456和author654321
     * {
     *         "_id" : ObjectId("5de11abd40595865204f5e77"),
     *         "name" : "book0",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 {
     *                         "name" : "author123456",
     *                         "country" : "China"
     *                 },
     *                 {
     *                         "name" : "author654321",
     *                         "country" : "China"
     *                 }
     *         ]
     * }
     * {
     *         "_id" : ObjectId("5de11abd40595865204f5e78"),
     *         "name" : "book1",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 {
     *                         "name" : "author123456",
     *                         "country" : "China"
     *                 },
     *                 {
     *                         "name" : "author654321",
     *                         "country" : "China"
     *                 }
     *         ]
     * }
     *......
     *......
     * {
     *         "_id" : ObjectId("5de11b6040595865205ea0b6"),
     *         "name" : "book999999",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 {
     *                         "name" : "author123456",
     *                         "country" : "China"
     *                 },
     *                 {
     *                         "name" : "author654321",
     *                         "country" : "China"
     *                 }
     *         ]
     * }
     *
     *
     *
     * 这是book2集合，里面也存放1000000本书，书的信息和book1集合一样，只不过子文档采用引用式设计
     *{
     *         "_id" : ObjectId("5de11b6240595865205ea0b7"),
     *         "name" : "book0",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 ObjectId("5de11a3d405958652041fe77"),
     *                 ObjectId("5de11a8d40595865204a1828")
     *         ]
     * }
     * {
     *         "_id" : ObjectId("5de11b6240595865205ea0b8"),
     *         "name" : "book1",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 ObjectId("5de11a3d405958652041fe77"),
     *                 ObjectId("5de11a8d40595865204a1828")
     *         ]
     * }
     * ......
     * ......
     * {
     *         "_id" : ObjectId("5de11c0040595865206de2f6"),
     *         "name" : "book999999",
     *         "publication year" : 2019,
     *         "authors" : [
     *                 ObjectId("5de11a3d405958652041fe77"),
     *                 ObjectId("5de11a8d40595865204a1828")
     *         ]
     * }
     */
    public static void generatrDataIntoLibraryDB(){
        /*与上面的一样，建立连接*/
        ServerAddress s=new ServerAddress("localhost",27017);

        /* 创建客户端*/
        MongoClient mc=new MongoClient(s);

        /* 防止已经有这个数据库，先把它删掉*/
        mc.dropDatabase("library");

        /* 连接到student数据库，如果没有这个数据库，会自动创建*/
        MongoDatabase md=mc.getDatabase("library");

        /*连接到author集合 */
        MongoCollection<Document> authorCollection = md.getCollection("author");

        long startMili2=System.currentTimeMillis();

        /*生成数据，逐个插入*/
        for (int i = 0; i < 1000000; i++) {
            Document author=new Document();
            author.append("name","author"+i);
            author.append("country","China");
            authorCollection.insertOne(author);
        }

        long endMili2=System.currentTimeMillis();
        System.out.println("生成author数据，总耗时为："+(endMili2-startMili2)+"毫秒");

        /*连接到book1集合 */
        MongoCollection<Document> book1Collection = md.getCollection("book1");

        long startMili3=System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Document d1 = new Document();
            d1.append("name", "book" + i);
            d1.append("publication year", 2019);

            /*为了创建内嵌式文档，所以需要一个List存储书的作者，每一个作者也是一个文档*/
            /*“authors”数组存储的是完整的作者信息*/
            ArrayList<Document> authors = new ArrayList<>();

            Document author1 = new Document();
            author1.append("name", "author123456");
            author1.append("country", "China");
            authors.add(author1);

            Document author2 = new Document();
            author2.append("name", "author654321");
            author2.append("country", "China");
            authors.add(author2);

            d1.append("authors", authors);

            /*插入这本书的信息到book1集合中*/
            book1Collection.insertOne(d1);

        }
        long endMili3=System.currentTimeMillis();
        System.out.println("生成book1(内嵌式文档)数据，总耗时为："+(endMili3-startMili3)+"毫秒");

        /*连接到book2集合*/
        MongoCollection<Document> book2Collection = md.getCollection("book2");

        long startMili4=System.currentTimeMillis();

        /*这里要创建的是引用式文档，所以存储的是ObjectId*/
        ArrayList<ObjectId> authorsOID=new ArrayList<>();
        Bson b1,b2,b;

        /*首先需要在author集合中查找author123456、author654321的ObjectId*/
        b1= Filters.eq("name","author123456");
        b2= Filters.eq("country","China");
        b=Filters.and(b1,b2);
        MongoCursor<Document> mongoCursor = authorCollection.find(b).iterator();
        authorsOID.add(new ObjectId(mongoCursor.next().get("_id").toString()));

        b1= Filters.eq("name","author654321");
        b2= Filters.eq("country","China");
        b=Filters.and(b1,b2);
        mongoCursor = authorCollection.find(b).iterator();
        authorsOID.add(new ObjectId(mongoCursor.next().get("_id").toString()));

        /*生成book2中的图书信息，全部采用引用式设计，“authors”数组只存储作者的ObjectId*/
        for (int i = 0; i < 1000000; i++) {
            Document d2=new Document();
            d2.append("name","book"+i);
            d2.append("publication year",2019);
            d2.append("authors",authorsOID);
            book2Collection.insertOne(d2);
        }
        long endMili4=System.currentTimeMillis();
        System.out.println("生成book2(引用式文档)数据，总耗时为："+(endMili4-startMili4)+"毫秒");
    }

}
