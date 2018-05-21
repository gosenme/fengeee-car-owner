package com.fengeee.car.owner.common.util;


import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;


/**
 * 基于雪花算法 实现分布式系统ID生成工具
 *
 * @author linxixin@cvte.com
 * @version 1.0
 */
@Slf4j
public class IdWorker {

    private static IdWorker idWorker;
    private final  long     workerId;
    /**起始标记点，作为基准*/
    private static final long SNS_EPOCH            = 1473154287634L;
    /**并发控制*/
    private              long sequence             = 0L;
    /**暂时只允许workid的范围为：0-1023*/
    private static final long WORKER_ID_BITS       = 10L;
    private static final long MAX_WORKER_ID        = -1L ^ -1L << WORKER_ID_BITS;
    /**sequence值控制在0-4095*/
    private static final long SEQUENCE_BITS        = 12L;
    private static final long SEQUENCE_BITS1       = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**4095,111111111111,12位*/
    private static final long SEQUENCE_MASK        = -1L ^ -1L << SEQUENCE_BITS;
    private long lastTimestamp = -1L;


    /**
     * 初始化
     * @param index 当前机器位置
     */
    public static void init(int index) {
        idWorker = new IdWorker(index);
    }

    public static void setIdWorker(IdWorker idWorker) {
        IdWorker.idWorker = idWorker;
    }

    public static BigInteger generateId() {
        try {
            return BigInteger.valueOf(idWorker.nextId());
        } catch (Exception e) {
            log.error("获取分布式唯一ID异常:{}", e);
        }
        return BigInteger.ZERO;
    }


    public IdWorker(long workerId) {
        super();
        /*workid < 1024[10位：2的10次方]*/
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() throws Exception {
        long timestamp = this.timeGen();
        /*如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环)，下次再使用时sequence是新值*/
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1 & SEQUENCE_MASK;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            log.error(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        }
        this.lastTimestamp = timestamp;
        // 生成的timestamp
        return ((timestamp - SNS_EPOCH) << TIMESTAMP_LEFT_SHIFT) | (this.workerId << SEQUENCE_BITS1) | this.sequence;
    }

    /**
     * 保证返回的毫秒数在参数之后
     *
     * @param lastTimestamp 最新毫秒
     * @return long
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     *
     * @return 系统当前时间
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

}
