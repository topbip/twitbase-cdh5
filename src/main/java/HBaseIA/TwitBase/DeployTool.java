package HBaseIA.TwitBase;

import HBaseIA.TwitBase.coprocessors.FollowsObserver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by junliang on 2017/4/4.
 */
public class DeployTool {

  public static void main(String[] args) throws IOException {

    TableName tableName = TableName.valueOf("follows");
    Path path = new Path("hdfs://<namenode>:<port>/user/<hadoop-user>/coprocessor.jar");
    Configuration conf = HBaseConfiguration.create();
    conf.set("hbase.zookeeper.quorum","quickstart.cloudera:2181");
    Connection connection = ConnectionFactory.createConnection(conf);
    Admin admin = connection.getAdmin();
    admin.disableTable(tableName);
    HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
    HColumnDescriptor columnFamily1 = new HColumnDescriptor("personalDet");
    columnFamily1.setMaxVersions(3);
    hTableDescriptor.addFamily(columnFamily1);
    HColumnDescriptor columnFamily2 = new HColumnDescriptor("salaryDet");
    columnFamily2.setMaxVersions(3);
    hTableDescriptor.addFamily(columnFamily2);
    hTableDescriptor.addCoprocessor(FollowsObserver.class.getCanonicalName(), path,
                                    Coprocessor.PRIORITY_USER, null);
    admin.modifyTable(tableName, hTableDescriptor);
    admin.enableTable(tableName);
  }

}
