package com.fengeee.car.owner.service.impl;

import com.fengeee.car.owner.api.OwnerService;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.seewo.mis.common.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seewo.mis.common.exception.BaseErrorsEnum.EXCEPTION;
import static com.seewo.mis.common.exception.BaseErrorsEnum.SUCCESS;

/**
 * @author wanggaoxiang@cvte.com
 * @since 1.0
 */
@Log4j2
@Service
public class OwnerServiceImpl implements OwnerService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private InfluxDB connectDatabase() {
        return InfluxDBFactory.connect("http://47.254.21.68:8086", "admin", "admin");
    }

    @Override
    public BaseResponse addOwnerFromFile(String fileName) {
        try {
            File file = new File("E:\\BaiduNetdiskDownload\\7.csv");
            CounterLine counter = new CounterLine();
            Files.readLines(file, Charset.forName("GBK"), counter);
            log.info("总行数:{}", counter.getResult());
        } catch (IOException e) {
            log.error("读取文件异常:{}", e);
            return new BaseResponse(EXCEPTION, "读取文件异常");
        }
        return new BaseResponse(SUCCESS);
    }

    class CounterLine implements LineProcessor<Integer> {
        private int rowNum = 0;

        @Override
        public boolean processLine(String line) {
            if (rowNum == 0) {
                rowNum++;
                return true;
            }
            List<String> list = Arrays.asList(line.split(",")).stream()
                    .map(s -> StringUtils.replaceAll(s, "\"", ""))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(list)) {
                return true;
            }
            String temp = list.get(0);
            if (StringUtils.isEmpty(temp) || !StringUtils.contains(temp, "-")) {
                return true;
            }
            long time = System.currentTimeMillis();
            if (!StringUtils.isEmpty(list.get(20)) && list.get(20).length() == 21) {
                time = LocalDateTime.parse(StringUtils.substring(list.get(20), 0, 19), formatter).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            }
            int index = StringUtils.indexOf(temp, "-");
            InfluxDB connection = connectDatabase();
            Point point = Point.measurement("carOwner")
                    .tag("city", StringUtils.substring(temp, 0, index))
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("createTime", time)
                    .addField("registerno", temp)
                    .addField("model", list.get(1))
                    .addField("color", list.get(3))
                    .addField("vhcname", list.get(4))
                    .addField("grade", list.get(5))
                    .addField("engine", list.get(6))
                    .addField("buyername", list.get(9))
                    .addField("buyeraddr", list.get(11))
                    .addField("buyertel1", list.get(12))
                    .addField("buyertel2", list.get(13))
                    .addField("buyerfax", list.get(14))
                    .addField("buyeremail", list.get(15))
                    .addField("mileage", list.get(18))
                    .addField("credate", list.get(20))
                    .addField("nomineestreet", list.get(21))
                    .addField("nomineemobil1", list.get(22))
                    .addField("fbuyername", list.get(23))
                    .addField("buyersex", list.get(24))
                    .addField("buyerages", list.get(25))
                    .addField("buyerstreet", list.get(26))
                    .addField("fbuyeraddress", list.get(27))
                    .addField("buyermobil1", list.get(28))
                    .addField("buyermobil2", list.get(29))
                    .addField("fbuyertel1", list.get(30))
                    .addField("fbuyertel2", list.get(31))
                    .addField("latestsndcustname", list.get(32))
                    .addField("latestsndcusttel1", list.get(33))
                    .build();
            log.info("行号:{} 帐号信息:{}", rowNum,point);
            connection.write("car", "defaultPolicy", point);
            connection.close();
            rowNum++;
            return true;
        }

        @Override
        public Integer getResult() {
            return rowNum;
        }
    }

}
