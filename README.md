#Mongo4s
A dsl to interact with [MongoDB](http://www.mongodb.org/) in scala using the [ReactiveMongo](http://reactivemongo.org) library.
This project is still in development but any requests are welcome. Feel free to file a bugs/enhancements
into [issues](https://github.com/kfang/mongo4s/issues).

#Building/Sonatype
This project is still in active development and doesn't have a stable version (yet). But its up on sonatype!
Its cross-published against scala version 2.11.6 and 2.10.5 so it should work with both. The snapshot is not
always up to date with whats in the repository. I'm sort of ["dogfooding"](http://en.wikipedia.org/wiki/Eating_your_own_dog_food)
this.
```scala
libraryDependencies += "com.github.kfang" %% "mongo4s" % "0.0.1-SNAPSHOT"
```

#Why this project exists
##Reason 1 => Verbosity
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
collection.execute(find sel "field" -> "value" skip 12 limit 24 asList)
```

##Reason 2 => CRUD
I'm okay with the IFUR(insert/find/update/remove) paradigm but not everyone is familiar with it and I don't think
it has a wikipedia page.  But a lot people know [CRUD(create/read/update/delete)](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete).
The dsl provides the verbage to interface the two. For example, these two lines are the same:
```scala
find("field" -> "value")
read("field" -> "value")
```
or
```scala
insert(BSONDocument("_id" -> 1))
create(BSONDocument("_id" -> 1))
```

Better documentation will exist once more of this stuff is implemented but the basics are in the
[FindDsl.scala](https://github.com/kfang/mongo4s/blob/master/src/main/scala/com/github/kfang/mongo4s/commands/FindDsl.scala)

#TODO
- [x] Find DSL
- [x] Insert DSL
- [x] Delete DSL
- [ ] Update DSL
- [x] Count DSL
- [ ] Distinct DSL

#Usage
##MongoConnPool
The MongoConnPool contains your MongoConnection. For every set of nodes you need to attach to, you have one
MongoConnection.  This one MongoConnPool is then shared by your MongoDatabases.
```scala
val system = ActorSystem()
val nodes = Seq("localhost")
val connPool = MongoConnPool(system, nodes)
```
##Connecting to a Database
```scala
case class SampleModel(_id: String, data: String)
object SampleModel { implicit val handler = Macros.handler[SampleModel] }

case class MyMongoDB(conn: MongoConnPool) extends MongoDatabase {
    val name: String = "db_name"
    val MyCollection: MongoCollection = getCollection[SampleModel]("my-collection")
}
```

##Using the DSL
```scala
import com.github.kfang.mongo4s.MongoDsl._

//connect to the database using the connection pool
val Database = MyMongoDB("my-db-name", connPool)

//execute a query on a collection
Database.MyCollection.execute(count("data" -> "data-value")).map(c => {
  println("There are " + c + " document that match {'data':'data-value'}")
})

//find/read out documents
val getOne: Future[Option[SampleModel]] = Database.MyCollection.execute(find.id("id-to-find").one)
val getOne: Future[Option[SampleModel]] = Database.MyCollection.execute(find.sel("_id" -> "id-to-find").one)
val getList: Future[List[SampleModel]] = Database.MyCollection.execute(find.sel().limit(20).asList)

//insert/create a document
val model = SampleModel("some-id", "my-data")
Database.MyCollection.execute(insert.model(model))
Database.MyCollection.execute(insert.doc(BSONDocument("_id" -> "some-id", "data" -> "my-data")))

//remove/delete a document
val model = SampleModel("some-id", "my-data")
Database.MyCollection.execute(remove.model(model))
Database.MyCollection.execute(remove.id(model.id))
Database.MyCollection.execute(remove.sel("_id" -> model.id))

//TODO: update a document
//TODO: distinct values
```