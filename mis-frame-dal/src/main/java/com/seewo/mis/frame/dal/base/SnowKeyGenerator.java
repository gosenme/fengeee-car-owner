package com.seewo.mis.frame.dal.base;


import com.seewo.mis.snowflake.generator.SnowflakeGenerator;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.List;

/**
 * @author linxixin@cvte.com
 * @since 1.0
 */
@Component
public class SnowKeyGenerator implements KeyGenerator {

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        for (String keyProperty : ms.getKeyProperties()) {
            if (parameter instanceof DefaultSqlSession.StrictMap) {
                List collection = (List) ((DefaultSqlSession.StrictMap) parameter).get("collection");
                for (Object o : collection) {
                    MetaObject metaObject = SystemMetaObject.forObject(o);
                    if (metaObject.getValue(keyProperty) == null) {
                        metaObject.setValue(keyProperty, snowflakeGenerator.nextId());
                    }
                }
            } else {
                MetaObject metaObject = SystemMetaObject.forObject(parameter);
                if (metaObject.getValue(keyProperty) == null) {
                    metaObject.setValue(keyProperty, snowflakeGenerator.nextId());
                }
            }
        }
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    }

}
