package cn.cutepikachu.datawisemaster.mybatisplus;

import cn.cutepikachu.datawisemaster.model.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Mybatis-Plus Generator
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class MyBatisPlusGeneratorTest {

    private static final String outputDir = System.getProperty("user.dir") + "/src/main/java";

    private static final String auth = "笨蛋皮卡丘";

    private static final String outputPackage = "cn.cutepikachu.generator";

    private static final String projectPackage = "cn.cutepikachu.datawisemaster";

    public static void main(String[] args) {
        generateCode();
    }

    public static void generateCode() {
        FastAutoGenerator
                // 数据源配置
                .create(new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/data_wise_master?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "root123456789")
                        .keyWordsHandler(new MySqlKeyWordsHandler()))
                // 全局配置
                .globalConfig(globalConfigBuilder -> globalConfigBuilder
                        .outputDir(outputDir) // 输出目录
                        .disableOpenDir() // 禁止打开输出目录
                        .author(auth) // 作者名
                        .enableSpringdoc() // 开启 springdoc 模式
                        .dateType(DateType.TIME_PACK) // 时间策略
                        .commentDate("yyyy-MM-dd HH:mm:ss") // 注释日期时间格式
                )
                .packageConfig(packageConfigBuilder -> packageConfigBuilder
                        .parent(outputPackage) // 父包名
                        // .moduleName("application") // 父包模块名
                        .entity("model.entity") // Entity 包名
                        .service("service") // Service 包名
                        .serviceImpl("service.impl") // Service Impl 包名
                        .mapper("mapper") // Mapper 包名
                        .xml("mapper.xml") // Mapper XML 包名
                        .controller("controller") // Controller 包名
                )
                // 策略配置
                .strategyConfig(strategyConfigBuilder -> strategyConfigBuilder
                        .enableCapitalMode()
                        .enableSkipView()
                        .entityBuilder()
                        // 实体策略配置
                        .enableColumnConstant() // 开启生成字段常量
                        .enableChainModel() // 开启链式模型
                        .enableLombok() // 开启 lombok 模型
                        .enableTableFieldAnnotation() // 开启生成实体时生成字段注解
                        .enableActiveRecord() // 开启 ActiveRecord 模型
                        .superClass(BaseEntity.class) // 继承的Entity类
                        .logicDeleteColumnName("is_delete") // 逻辑删除字段名(数据库字段)
                        .naming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                        .columnNaming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE)
                        ) // 添加表字段填充
                        .idType(IdType.ASSIGN_ID) // 全局主键类型
                        .enableFileOverride() // 覆盖文件

                        // controller 策略配置
                        .controllerBuilder()
                        .enableRestStyle() // 开启生成 @RestController 控制器
                        .enableFileOverride() // 覆盖文件
                        .enableHyphenStyle()

                        // mapper 策略配置
                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class) // 开启 @Mapper 注解
                        .enableBaseResultMap() // 启用 BaseResultMap 生成
                        .enableBaseColumnList() // 启用 BaseColumnList
                        .enableFileOverride() // 覆盖文件

                        // service 策略配置
                        .serviceBuilder()
                        .enableFileOverride() // 覆盖文件
                )
                // 模板配置
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(templateConfigBuilder -> templateConfigBuilder
                        .disable()
                        .entity("/templates/model/entity/entity.java")
                        .service("/templates/service/service.java")
                        .serviceImpl("/templates/service/impl/serviceImpl.java")
                        .mapper("/templates/mapper/mapper.java")
                        .xml("/templates/mapper/xml/mapper.xml")
                        .controller("/templates/controller/controller.java")
                )
                // 注入配置
                .injectionConfig(injectionConfigBuilder -> {
                    CustomFile VOFile = new CustomFile.Builder()
                            .fileName("VO.java")
                            .packageName("model.vo")
                            .templatePath("/templates/model/vo/vo.java.ftl")
                            .enableFileOverride()
                            .build();
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("projectPackage", projectPackage);
                    injectionConfigBuilder.customFile(VOFile);
                    injectionConfigBuilder.customMap(customMap);
                })
                .execute();
        ;
    }
}
