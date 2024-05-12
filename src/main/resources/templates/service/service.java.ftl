<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
package ${package.Service};

import ${package.Parent}.model.dto.${entity}QueryRequest;
import ${package.Entity}.${entity};
import ${package.Parent}.model.vo.${vo};
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @date ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    /**
     * 获取查询条件
     *
     * @param ${entityUnCapFirst}QueryRequest ${entity} 查询请求参数
     * @return lambda 查询条件
     */
    LambdaQueryWrapper<${entity}> getLambdaQueryWrapper(${entity}QueryRequest ${entityUnCapFirst}QueryRequest);
     /**
     * 根据 Entity 获取 VO
     *
     * @param ${entityUnCapFirst} ${entity} 对象
     * @return VO
     */
    ${vo} get${vo}(${entity} ${entityUnCapFirst});
    /**
     * 根据 Entity 分页获取 VO 分页
     *
     * @param ${entityUnCapFirst}Page ${entity} 分页对象
     * @return 分页 VO
     */
    Page<${vo}> get${vo}Page(Page<${entity}> ${entityUnCapFirst}Page);
}
</#if>
