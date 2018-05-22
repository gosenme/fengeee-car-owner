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

    private static final DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String            dbName     = "baeldung";
    private              InfluxDB          connection = connectDatabase();

    private InfluxDB connectDatabase() {
        return InfluxDBFactory.connect("http://47.254.21.68:8086", "admin", "admin");
    }

    @Override
    public BaseResponse addOwnerFromFile(String fileName) {
        try {
            File file = new File("E:\\BaiduNetdiskDownload\\test.csv");
            CounterLine counter = new CounterLine();
            Files.readLines(file, Charset.forName("UTF-8"), counter);
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
        public boolean processLine(String line) throws IOException {
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
            Point point = Point.measurement("car-owner")
                    .tag("city", StringUtils.substring(temp, 0, index))
                    .time(time, TimeUnit.MILLISECONDS)
                    .addField("registerno", temp)
                    .addField("model", list.get(1))
                    //.addField("sfx", list.get(2))
                    .addField("color", list.get(3))
                    .addField("vhcname", list.get(4))
                    .addField("grade", list.get(5))
                    .addField("engine", list.get(6))
                    //.addField("salesdealercode", list.get(7))
                    //.addField("crdealercode", list.get(8))
                    .addField("buyername", list.get(9))
                    //.addField("buyerid", list.get(10))
                    .addField("buyeraddr", list.get(11))
                    .addField("buyertel1", list.get(12))
                    .addField("buyertel2", list.get(13))
                    .addField("buyerfax", list.get(14))
                    .addField("buyeremail", list.get(15))
                    //.addField("latestsrvdate1", list.get(16))
                    //.addField("nextsrvdate", list.get(17))
                    .addField("mileage", list.get(18))
                    //.addField("vhcsalesdate", list.get(19))
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
            connection.write(dbName, "defaultPolicy", point);
            log.info("帐号信息:{}", point);

            rowNum++;
            return true;
        }

        @Override
        public Integer getResult() {
            return rowNum;
        }
    }

}
