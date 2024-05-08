<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
package ${package.Controller};

import ${projectPackage}.util.ThrowUtil;
import ${projectPackage}.util.ResponseUtil;
import ${projectPackage}.common.ResponseCode;
import ${projectPackage}.common.BaseResponse;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import javax.validation.Valid;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Resource
    private ${table.serviceName} ${entityUnCapFirst}Service;
    /**
     * 增
     *
     * @param ${entityUnCapFirst}AddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> add${entity}(@RequestBody @Valid ${entity}AddRequest ${entityUnCapFirst}AddRequest) {
        ${entity} ${entityUnCapFirst} = new ${entity}();
        BeanUtil.copyProperties(${entityUnCapFirst}AddRequest, ${entityUnCapFirst});
        boolean result = ${entityUnCapFirst}Service.save(${entityUnCapFirst});
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        long new${entity}Id = ${entityUnCapFirst}.getId();
        return ResponseUtil.success(new${entity}Id);
    }
}
</#if>
