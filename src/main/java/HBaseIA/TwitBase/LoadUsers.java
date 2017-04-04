package HBaseIA.TwitBase;

import HBaseIA.TwitBase.hbase.UsersDAO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Table;
import utils.LoadUtils;

import java.io.IOException;
import java.util.List;

public class LoadUsers {

  public static final String usage =
    "loadusers count\n" +
    "  help - print this message and exit.\n" +
    "  count - add count random TwitBase users.\n";

  private static String randName(List<String> names) {
    String name = LoadUtils.randNth(names) + " ";
    name += LoadUtils.randNth(names);
    return name;
  }

  private static String randUser(String name) {
    return String.format("%s%2d", name.substring(5), LoadUtils.randInt(100));
  }

  private static String randEmail(String user, List<String> words) {
    return String.format("%s@%s.com", user, LoadUtils.randNth(words));
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0 || "help".equals(args[0])) {
      System.out.println(usage);
      System.exit(0);
    }

    // Create a connection to the cluster.
    Configuration conf = HBaseConfiguration.create();
    conf.set("hbase.zookeeper.quorum","quickstart.cloudera:2181");

    Connection connection = ConnectionFactory.createConnection(conf);

    UsersDAO dao = new UsersDAO(connection);

    int count = Integer.parseInt(args[0]);
    List<String> names = LoadUtils.readResource(LoadUtils.NAMES_PATH);
    List<String> words = LoadUtils.readResource(LoadUtils.WORDS_PATH);

    for (int i = 0; i < count; i++) {
      String name = randName(names);
      String user = randUser(name);
      String email = randEmail(user, words);
      dao.addUser(user, name, email, "abc123");
    }

  }
}
