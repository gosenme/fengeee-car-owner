package com.seewo.mis.frame.service.impl;

import com.alibaba.fastjson.JSON;
import com.seewo.mis.frame.common.exception.BaseException;
import com.seewo.mis.frame.common.util.IPUtils;
import com.seewo.mis.frame.common.util.IdWorker;
import com.seewo.mis.frame.service.IdWorkerService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.seewo.mis.frame.constant.ErrorsEnum.GET_SNOWFLAKE_FAIL;

/**
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-12
 * @version 1.0
 */
@Slf4j
@Service
public class IdWorkerServiceImpl implements IdWorkerService {

    @Value("${id.worker.zookeeper.address}")
    private String registryAddress;
    @Value("${server.context-path}")
    private String servicePath;

    private static final String SEEWO_MIS_SNOW_SNOW      = "/mis/snow";
    private static final String SEEWO_MIS_SNOW_SNOW_NODE = "/mis/snow/snow-node";
    private static final int    SLEEP_MILLIS             = 3000;
    private static final int    ZK_SESSION_TIMEOUT       = 10000;
    private static final int    ZK_CONNECTION_TIMEOUT    = 10000;
    private static final int    RETRY_TIMES              = 10000;
    private static       int    SNOW_KEY                 = -1;

    @PostConstruct
    public void initStatic(){
        log.info("[雪花算法]初始化,开始上报IP，Mac地址到zk");
        IdWorker.init(getSnowKey());
    }

    @Override
    public int getSnowKey() {
        if (SNOW_KEY == -1) {
            try {
                SNOW_KEY = registerMac();
            } catch (Exception e) {
                throw new BaseException(GET_SNOWFLAKE_FAIL,e.getMessage());
            }
            return SNOW_KEY;
        } else {
            return SNOW_KEY;
        }
    }

    private int registerMac() throws IOException, InterruptedException {
        ZkClient zkClient = new ZkClient(registryAddress, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new ZkSerializer() {
            @Override
            public byte[] serialize(Object data) {
                return JSON.toJSONString(data).getBytes();
            }

            @Override
            public Object deserialize(byte[] bytes) {
                return JSON.parseObject(new String(bytes), SnowKey.class);
            }
        });

        if (!zkClient.exists(SEEWO_MIS_SNOW_SNOW)) {
            zkClient.createPersistent(SEEWO_MIS_SNOW_SNOW, true);
        }

        int count = 0;
        String lockPath = SEEWO_MIS_SNOW_SNOW + "/lock";
        while (true) {
            try {
                zkClient.createEphemeral(lockPath);
            } catch (Exception e) {
                log.error("获取雪花的锁失败 准备重新握手 time:" + count++, e);
                if (count > RETRY_TIMES) {
                    throw new BaseException(GET_SNOWFLAKE_FAIL);
                }
                Thread.sleep(SLEEP_MILLIS);
                continue;
            }
            List<String> children = zkClient.getChildren(SEEWO_MIS_SNOW_SNOW);
            List<Object> collect = children.stream().map(path -> zkClient.readData(SEEWO_MIS_SNOW_SNOW + "/" + path))
                    .collect(Collectors.toList());
            List<Integer> orders = new ArrayList<>();
            for (Object o : collect) {
                if (o == null) {
                    continue;
                }
                SnowKey key = (SnowKey) o;
                orders.add(key.getOrder());
            }
            Collections.sort(orders);
            int matchOrder = getMatchOrder(orders);

            zkClient.createEphemeralSequential(SEEWO_MIS_SNOW_SNOW_NODE, new SnowKey(IPUtils.getLocalIP(), matchOrder, servicePath));
            zkClient.delete(lockPath);
            return matchOrder;
        }

    }

    private int getMatchOrder(List<Integer> orders) {
        for (int i = 0; i < orders.size(); i++) {
            if (!orders.get(i).equals(i)) {
                return i;
            }
        }
        return orders.size();
    }

    /**
     * 雪花KEY
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SnowKey {
        private String  ip;
        private Integer order;
        private String  project;
    }
}
