package com.baidu.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ZookeeperApplicationTests {

    /**
     * 客户端连接zookeeper
     * @throws IOException
     */
    @Test
    void contextLoads() throws IOException {
        String connectString = "CentOSA:2181,CentOSB:2181,CentOSC:2181";
        int sessionTimeout =5000;
        ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("执行..");
            }
        });
        System.out.println("完毕.."+zooKeeper);
    }

}
