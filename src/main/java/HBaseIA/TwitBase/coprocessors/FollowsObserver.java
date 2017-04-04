package HBaseIA.TwitBase.coprocessors;

import HBaseIA.TwitBase.hbase.RelationsDAO;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

import static HBaseIA.TwitBase.hbase.RelationsDAO.FOLLOWS_TABLE_NAME;
import static HBaseIA.TwitBase.hbase.RelationsDAO.FROM;
import static HBaseIA.TwitBase.hbase.RelationsDAO.RELATION_FAM;
import static HBaseIA.TwitBase.hbase.RelationsDAO.TO;

public class FollowsObserver extends BaseRegionObserver {

  private Connection connection = null;

  @Override
  public void start(CoprocessorEnvironment env) throws IOException {
    connection = ConnectionFactory.createConnection(env.getConfiguration());
  }

  @Override
  public void stop(CoprocessorEnvironment env) throws IOException {
    connection.close();
  }

  @Override
  public void postPut(
    ObserverContext<RegionCoprocessorEnvironment> e,
    Put put,
    WALEdit edit,
    Durability durability
  ) throws IOException {

    byte[] table = e.getEnvironment().getRegionInfo().getTable().getName();
    if (!Bytes.equals(table, FOLLOWS_TABLE_NAME)) {
      return;
    }

    Cell cell = put.get(RELATION_FAM, FROM).get(0);
    String from = Bytes.toString(cell.getValueArray());
    cell = put.get(RELATION_FAM, TO).get(0);
    String to = Bytes.toString(cell.getValueArray());

    RelationsDAO relations = new RelationsDAO(connection);
    relations.addFollowedBy(to, from);
  }
}
