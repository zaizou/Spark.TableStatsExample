package com.cloudera.sa.examples.tablestats

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, LongType, StructField, StructType}
import org.scalatest.{FunSuite, BeforeAndAfterEach, BeforeAndAfterAll}

/**
 * Created by ted.malaska on 7/1/15.
 */
class TestTableStatsSinglePathMain extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll{
  test("run table stats on sample data") {

    val sparkConfig = new SparkConf()
    sparkConfig.set("spark.broadcast.compress", "false")
    sparkConfig.set("spark.shuffle.compress", "false")
    sparkConfig.set("spark.shuffle.spill.compress", "false")
    var sc = new SparkContext("local", "test", sparkConfig)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val schema =
      StructType(
        Array(
          StructField("id", LongType, true),
          StructField("name", StringType, true),
          StructField("age", LongType, true),
          StructField("gender", StringType, true),
          StructField("height", LongType, true),
          StructField("job_title", StringType, true)
        )
      )

    val rowRDD = sc.parallelize(Array(
      Row(1l, "Name.1", 20l, "M", 6l, "dad"),
      Row(2l, "Name.2", 20l, "F", 5l, "mom"),
      Row(3l, "Name.3", 20l, "F", 5l, "mom"),
      Row(4l, "Name.4", 20l, "F", 5l, "mom"),
      Row(5l, "Name.5", 10l, "M", 4l, "kid"),
      Row(6l, "Name.6", 8l, "M", 3l, "kid")))

    val df = sqlContext.createDataFrame(rowRDD, schema)

    val firstPassStats = TableStatsSinglePathMain.getFirstPassStat(df)
  }
}
