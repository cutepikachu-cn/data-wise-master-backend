<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
package ${package.ServiceImpl};

<#list table.importPackages as pkg>
    <#if !pkg?contains("mybatisplus") && !pkg?contains("BaseEntity") && !pkg?contains("Serializable")>
import ${pkg};
    </#if>
</#list>
import ${projectPackage}.model.enums.SortOrder;
import ${package.Parent}.model.dto.${entity}QueryRequest;
import ${package.Entity}.${entity};
import ${package.Parent}.model.vo.${vo};
import ${package.Mapper}.${table.mapperName};
<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
import ${projectPackage}.util.SqlUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @date ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if table.serviceInterface>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if table.serviceInterface> implements ${table.serviceName}</#if> {
    @Override
    public LambdaQueryWrapper<${entity}> getLambdaQueryWrapper(${entity}QueryRequest queryRequest) {
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return lambdaQueryWrapper;
        }
<#list table.fields as field>
    <#if !field.logicDeleteField>
        ${field.propertyType} ${field.propertyName} = queryRequest.get${field.propertyName?cap_first}();
    </#if>
</#list>
<#list table.fields as field>
    <#if !field.logicDeleteField>
        lambdaQueryWrapper.like(ObjectUtil.isNotEmpty(${field.propertyName}), ${entity}::get${field.propertyName?cap_first}, ${field.propertyName});
    </#if>
</#list>

        String sortField = queryRequest.getSortField();
        SortOrder sortOrder = queryRequest.getSortOrder();
        if (SqlUtil.isValidSqlInput(sortField)) {
            boolean isAsc = sortOrder == SortOrder.ASCENDING;
            switch (sortField) {
<#list table.fields as field>
    <#if !field.logicDeleteField>
                case "${field.propertyName}":
                    lambdaQueryWrapper.orderBy(true, isAsc, ${entity}::get${field.propertyName?cap_first});
                    break;
    </#if>
</#list>
            }
        }

        return lambdaQueryWrapper;
    }
    @Override
    public ${vo} get${vo}(${entity} ${entityUnCapFirst}) {
        return ${entityUnCapFirst}.toVO(${vo}.class);
    }
    @Override
    public Page<${vo}> get${vo}Page(Page<${entity}> ${entityUnCapFirst}Page) {
        List<${entity}> ${entityUnCapFirst}List = ${entityUnCapFirst}Page.getRecords();
        Page<${vo}> ${entityUnCapFirst}VOPage = new Page<>(${entityUnCapFirst}Page.getCurrent(), ${entityUnCapFirst}Page.getSize(), ${entityUnCapFirst}Page.getTotal());
        if (${entityUnCapFirst}List.isEmpty()) {
            return ${entityUnCapFirst}VOPage;
        }
        List<${vo}> ${entityUnCapFirst}VOList = ${entityUnCapFirst}List.stream().map(this::get${vo}).collect(Collectors.toList());
        ${entityUnCapFirst}VOPage.setRecords(${entityUnCapFirst}VOList);
        return ${entityUnCapFirst}VOPage;
    }
}
</#if>
