<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
package ${package.ServiceImpl};

import ${package.Parent}.model.dto.${entity}QueryRequest;
import ${package.Entity}.${entity};
import ${package.Parent}.model.vo.${vo};
import ${package.Mapper}.${table.mapperName};
<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
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
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if table.serviceInterface>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if table.serviceInterface> implements ${table.serviceName}</#if> {
    @Override
    public LambdaQueryWrapper<${entity}> getLambdaQueryWrapper(${entity}QueryRequest ${entityUnCapFirst}QueryRequest) {
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (${entityUnCapFirst}QueryRequest == null) {
            return lambdaQueryWrapper;
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
        List<${vo}> ${entityUnCapFirst}VOList = ${entityUnCapFirst}List.stream().map(${entityUnCapFirst} -> ${entityUnCapFirst}.toVO(${vo}.class)).collect(Collectors.toList());
        ${entityUnCapFirst}VOPage.setRecords(${entityUnCapFirst}VOList);
        return ${entityUnCapFirst}VOPage;
    }
}
</#if>
