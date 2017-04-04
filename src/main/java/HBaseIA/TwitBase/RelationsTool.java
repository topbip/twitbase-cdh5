package HBaseIA.TwitBase;

import HBaseIA.TwitBase.hbase.RelationsDAO;
import HBaseIA.TwitBase.model.Relation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RelationsTool {

  private static final Logger log = Logger.getLogger(RelationsTool.class);

  private static final String usage =
    "relationstool action ...\n" +
    "  help - print this message and exit.\n" +
    "  follows fromId toId - add a new relationship where from follows to.\n" +
    "  list follows userId - list everyone userId follows.\n" +
    "  list followedBy userId - list everyone who follows userId.\n" +
    "  followedByScan userId - count users' followers using a client-side scanner" +
    "  followedByCoproc userId - count users' followers using the Endpoint coprocessor";

  public static void main(String[] args) throws Throwable {
    if (args.length == 0 || "help".equals(args[0])) {
      System.out.println(usage);
      System.exit(0);
    }

    // Create a connection to the cluster.
    Configuration conf = HBaseConfiguration.create();
    conf.set("hbase.zookeeper.quorum","quickstart.cloudera:2181");

    Connection connection = ConnectionFactory.createConnection(conf);

    //HTablePool pool = new HTablePool();
    RelationsDAO dao = new RelationsDAO(connection);

    if ("follows".equals(args[0])) {
      log.debug(String.format("Adding follower %s -> %s", args[1], args[2]));
      dao.addFollows(args[1], args[2]);
      System.out.println("Successfully added relationship");
    }

    if ("list".equals(args[0])) {
      List<Relation> results = new ArrayList<Relation>();
      if (args[1].equals("follows"))
        results.addAll(dao.listFollows(args[2]));
      else if (args[1].equals("followedBy"))
        results.addAll(dao.listFollowedBy(args[2]));

      if (results.isEmpty())
        System.out.println("No relations found.");
      for (Relation r : results) {
        System.out.println(r);
      }
    }

    if ("followedByScan".equals(args[0])) {
      long count = dao.followedByCountScan(args[1]);
      System.out.println(String.format("%s has %s followers.", args[1], count));
    }

    //if ("followedByCoproc".equals(args[0])) {
    //  long count = dao.followedByCount(args[1]);
    //  System.out.println(String.format("%s has %s followers.", args[1], count));
    //}

    //
    //pool.closeTablePool(RelationsDAO.FOLLOWS_TABLE_NAME);
    //pool.closeTablePool(RelationsDAO.FOLLOWED_TABLE_NAME);
    connection.close();
  }
}
