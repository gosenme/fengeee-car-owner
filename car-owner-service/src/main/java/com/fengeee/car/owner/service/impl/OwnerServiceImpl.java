package com.fengeee.car.owner.service.impl;

import com.fengeee.car.owner.api.OwnerService;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.seewo.mis.common.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static com.seewo.mis.common.exception.BaseErrorsEnum.EXCEPTION;
import static com.seewo.mis.common.exception.BaseErrorsEnum.SUCCESS;

/**
 * @author wanggaoxiang@cvte.com
 * @since 1.0
 */
@Log4j2
@Service
public class OwnerServiceImpl implements OwnerService {

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
            String[] strings = StringUtils.splitByWholeSeparator(line, ",");
            if (StringUtils.trim(strings[0]) != null
                    && (StringUtils.trim(strings[0]).equals("\"\"")
                    || !StringUtils.contains(strings[0], "-"))) {
                return true;
            }
            log.info("帐号信息:{}", line);
            rowNum++;
            return true;
        }

        @Override
        public Integer getResult() {
            return rowNum;
        }
    }

}
