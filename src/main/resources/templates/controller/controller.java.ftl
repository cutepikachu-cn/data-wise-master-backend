<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
<#assign voUnCapFirst=vo?uncap_first>
<#assign service>${entityUnCapFirst}Service</#assign>
package ${package.Controller};

import ${projectPackage}.util.ThrowUtil;
import ${projectPackage}.util.ResponseUtil;
import ${projectPackage}.common.DeleteRequest;
import ${projectPackage}.common.ResponseCode;
import ${projectPackage}.common.BaseResponse;
import ${package.Parent}.model.dto.${entity}CreateRequest;
import ${package.Parent}.model.dto.${entity}UpdateRequest;
import ${package.Parent}.model.dto.${entity}QueryRequest;
import ${package.Parent}.model.vo.${vo};
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.validation.Valid;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @date ${date}
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
    private ${table.serviceName} ${service};
    /**
     * 增
     *
     * @param ${entityUnCapFirst}CreateRequest 创建请求参数
     */
    @PostMapping("/create")
    public BaseResponse<Long> create${entity}(@RequestBody @Valid ${entity}CreateRequest ${entityUnCapFirst}CreateRequest) {
        ${entity} ${entityUnCapFirst} = new ${entity}();
        BeanUtil.copyProperties(${entityUnCapFirst}CreateRequest, ${entityUnCapFirst});
        boolean result = ${service}.save(${entityUnCapFirst});
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        long new${entity}Id = ${entityUnCapFirst}.getId();
        return ResponseUtil.success(new${entity}Id);
    }
    /**
     * 删
     *
     * @param deleteRequest 删除请求参数
     */
    @PostMapping("/delete")
    public BaseResponse<?> delete${entity}(@RequestBody @Valid DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        ${entity} old${entity} = ${service}.getById(id);
        ThrowUtil.throwIf(old${entity} == null, ResponseCode.NOT_FOUND_ERROR);
        boolean success = ${service}.removeById(id);
        ThrowUtil.throwIf(!success, ResponseCode.OPERATION_ERROR, "删除失败");
        return ResponseUtil.success();
    }
    /**
     * 改
     *
     * @param ${entityUnCapFirst}UpdateRequest 更新请求参数
     */
    @PostMapping("/update")
    public BaseResponse<?> update${entity}(@RequestBody @Valid ${entity}UpdateRequest ${entityUnCapFirst}UpdateRequest) {
        ${entity} ${entityUnCapFirst} = new ${entity}();
        BeanUtil.copyProperties(${entityUnCapFirst}UpdateRequest, ${entityUnCapFirst});
        Long id = ${entityUnCapFirst}.getId();
        ${entity} old${entity} = ${service}.getById(id);
        ThrowUtil.throwIf(old${entity} == null, ResponseCode.NOT_FOUND_ERROR);
        boolean success = ${service}.updateById(${entityUnCapFirst});
        ThrowUtil.throwIf(!success, ResponseCode.OPERATION_ERROR, "更新失败");
        return ResponseUtil.success();
    }
    /**
     * 根据 id 查询
     *
     * @param id ID
     */
    @GetMapping("/get")
    public BaseResponse<${entity}> get${entity}ById(@RequestParam long id) {
        ${entity} ${entityUnCapFirst} = ${service}.getById(id);
        ThrowUtil.throwIf(${entityUnCapFirst} == null, ResponseCode.NOT_FOUND_ERROR);
        return ResponseUtil.success(${entityUnCapFirst});
    }
    /**
     * 根据 id 查询 VO
     *
     * @param id ID
     */
    @GetMapping("/get/vo")
    public BaseResponse<${vo}> get${vo}ById(@RequestParam long id) {
        ${entity} ${entityUnCapFirst} = ${service}.getById(id);
        ThrowUtil.throwIf(${entityUnCapFirst} == null, ResponseCode.NOT_FOUND_ERROR);
        ${entity}VO ${voUnCapFirst} = ${service}.get${vo}(${entityUnCapFirst});
        return ResponseUtil.success(${voUnCapFirst});
    }
    /**
     * 分页查询
     *
     * @param ${entityUnCapFirst}QueryRequest 分页查询请求参数
     */
    @PostMapping("/page")
    public BaseResponse<Page<${entity}>> page${entity}(@RequestBody @Valid ${entity}QueryRequest ${entityUnCapFirst}QueryRequest) {
        Integer current = ${entityUnCapFirst}QueryRequest.getCurrent();
        Integer size = ${entityUnCapFirst}QueryRequest.getPageSize();
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = ${service}.getLambdaQueryWrapper(${entityUnCapFirst}QueryRequest);
        Page<${entity}> ${entityUnCapFirst}Page = ${service}.page(new Page<>(current, size), lambdaQueryWrapper);
        return ResponseUtil.success(${entityUnCapFirst}Page);
    }
    /**
     * 分页查询 VO
     *
     * @param ${entityUnCapFirst}QueryRequest 分页查询请求参数
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<${vo}>> page${vo}(@RequestBody @Valid ${entity}QueryRequest ${entityUnCapFirst}QueryRequest) {
        Integer current = ${entityUnCapFirst}QueryRequest.getCurrent();
        Integer size = ${entityUnCapFirst}QueryRequest.getPageSize();
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = ${service}.getLambdaQueryWrapper(${entityUnCapFirst}QueryRequest);
        Page<${entity}> ${entityUnCapFirst}Page = ${service}.page(new Page<>(current, size), lambdaQueryWrapper);
        Page<${vo}> ${voUnCapFirst}Page = ${service}.get${vo}Page(${entityUnCapFirst}Page);
        return ResponseUtil.success(${voUnCapFirst}Page);
    }
}
</#if>
