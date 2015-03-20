#Mongo4s
A dsl to interact with MongoDB in scala using the [ReactiveMongo](http://reactivemongo.org) library.
This project is still in development but any requests are welcome. Feel free to file a bugs/enhancements
into [issues](https://github.com/kfang/mongo4s/issues).

#Building/Sonatype
This project is still in active development and doesn't have a stable version (yet). But its up on sonatype!
Its +published against scala version 2.11.6 and 2.10.5 so it should work with both.
```scala
libraryDependencies += "com.github.kfang" %% "mongo4s" % "0.0.1-SNAPSHOT"
```

#Why this project exists
Rather than doing this:
```scala
collection.find(BSONDocument("field" -> "value")).options(QueryOpts(skipN = 12)).cursor[BSONDocument].collect[List](upTo = 24)
```
I'd rather do this:
```scala
collection.execute(find("field" -> "value").skip(12).limit(24).asList)
```
or if you don't like the dots:
```scala
collection.execute(
    find sel "field" -> "value" skip 12 limit 24 asList
)
```
Better documentation will exist once more of this stuff is implemented but the basics are in the
[FindDsl.scala](https://github.com/kfang/mongo4s/blob/master/src/main/scala/com/github/kfang/mongo4s/commands/FindDsl.scala)

#TODO
- [x] Find DSL
- [x] Insert DSL
- [ ] Delete DSL
- [ ] Update DSL
- [x] Count DSL
- [x] Distinct DSL

#Usage
##MongoConnPool
```scala
val system = ActorSystem()
val nodes = Seq("localhost")
val connPool = MongoConnPool(system, nodes)
```
##Connecting to a Database
```scala
case class SampleModel(_id: String, data: String)
object SampleModel { implicit val handler = Macros.handler[SampleModel] }

case class MyMongoDB(name: String, conn: MongoConnPool) extends MongoDatabase {
    val MyCollection: MongoCollection = getCollection[SampleModel]("my-collection")
}
```

##Using the DSL
```scala
import com.github.kfang.mongo4s.MongoDsl._

//connect to the database using the connection pool
val Database = MyMongoDB("my-db-name", connPool)

//execute a query on a collection
Database.MyCollection.execute(
    count("data" -> "data-value")
).map(c => {
    println("There are " + c + " document that match {'data':'data-value'}")
})
```