package cn.cutepikachu.datawisemaster.config;

import cn.cutepikachu.datawisemaster.model.enums.GenStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元对象字段填充控制器，元对象字段填充控制器
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入填充
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "genStatus", GenStatus.WAIT::getText, String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新填充
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

}
