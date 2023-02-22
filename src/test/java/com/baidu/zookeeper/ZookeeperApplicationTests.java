package com.baidu.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class ZookeeperApplicationTests {

    ZookeeperApplicationTests() throws IOException {
    }

    String connectString = "CentOSA:2181,CentOSB:2181,CentOSC:2181";
    int sessionTimeout =5000;

    ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("执行..");
        }
    });

    /**
     * 创建节点
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void create() throws InterruptedException, KeeperException {
        System.out.println(zooKeeper);

        String path = zooKeeper.create("/appTest", //创建的节点
                "HelloWorld".getBytes(), //节点内的值
                ZooDefs.Ids.OPEN_ACL_UNSAFE,//权限参数
                CreateMode.PERSISTENT); // 创建节点类型
        System.out.println(path);
    }

    /**
     * 盘点数据节点是是否存在
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void exist() throws InterruptedException, KeeperException {
        Stat stat = zooKeeper.exists("/appTest1", true);
        if(stat != null){
            System.out.println("stat:"+stat.toString());
        }else {
            String s = zooKeeper.create("/appTest1", "HelloWorld".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(s);
        }
    }

    /**
     * 获取某节点中的所有子节点
     * 获取所有子节点的所有数据
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void Childrens() throws InterruptedException, KeeperException {
        List<String> childrens = zooKeeper.getChildren("/app1", true);
        for (String children: childrens) {
           //获取哦所有子节点的数据
            byte[] data = zooKeeper.getData("/app1/" + children, true, null);
            System.out.println("children:"+new String(data));
        }
    }


    /**
     * 修改节点信息
     */
    @Test
    public void setData() throws InterruptedException, KeeperException {
        //-1 表示自动维护(不指定版本)
        Stat stat = zooKeeper.setData("/app1/innerapp1", "555".getBytes(), -1);
        System.out.println(stat);
    }

    /**
     * 非空的节点是不能被删除的,api没有提供删除非空节点的方法(不希望这样的操作)
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void delete() throws InterruptedException, KeeperException {
        zooKeeper.delete("/app1",-1);
    }

    /**
     * 子节点发生变化后的监控
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void nodeChildrenChange() throws InterruptedException, KeeperException {
        List<String> childrens = zooKeeper.getChildren("/app1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType());
            }
        });
        for (String children: childrens) {
            System.out.println(children);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 监听节点内容变更
     *  watchedEvent 对象监听的节点类型
     *  None(-1),
     *             NodeCreated(1),
     *             NodeDeleted(2),
     *             NodeDataChanged(3),
     *             NodeChildrenChanged(4),
     *             DataWatchRemoved(5),
     *             ChildWatchRemoved(6);
     */
    @Test
    public void nodeDataChange() throws InterruptedException, KeeperException {
        byte[] data = zooKeeper.getData("/app1/innerapp1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType());
            }
        }, null);
        System.out.println(new String(data));
        Thread.sleep(Integer.MAX_VALUE);
    }
 }
