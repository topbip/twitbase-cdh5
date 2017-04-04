Install quickstart.cloudera
===============================
hosts
-----------

# quickstart.cloudera
192.168.234.147  quickstart.cloudera  quickstart.cloudera

===============================

ssh cloudera@quickstart.cloudera
password: cloudera

===============================

http://quickstart.cloudera:7180/

===============================

file:///home/cloudera／twitbase-cdh5-1.0-SNAPSHOT.jar


HBaseIA.TwitBase.RelationsTool

java -cp twitbase-cdh5-1.0-SNAPSHOT.jar HBaseIA.TwitBase.RelationsTool

================

disable 'follows'

alter 'follows',METHOD => 'table_att', 'Coprocessor'=>'file:///home/cloudera/twitbase-cdh5-1.0-SNAPSHOT.jar|HBaseIA.TwitBase.coprocessors.FollowsObserver|1001|'

enable 'follows'

================

java -cp twitbase-cdh5-1.0-SNAPSHOT.jar HBaseIA.TwitBase.RelationsTool follows TheRealMT SirDoyle

java -cp twitbase-cdh5-1.0-SNAPSHOT.jar HBaseIA.TwitBase.RelationsTool list follows TheRealMT

java -cp twitbase-cdh5-1.0-SNAPSHOT.jar HBaseIA.TwitBase.RelationsTool list followedBy SirDoyle