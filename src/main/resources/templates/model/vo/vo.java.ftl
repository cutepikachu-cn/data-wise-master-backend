<#assign entityUnCapFirst=entity?uncap_first>
<#assign vo>${entity}VO</#assign>
package ${package.Parent}.model.vo;

<#list table.importPackages as pkg>
    <#if !pkg?contains("mybatisplus") && !pkg?contains("BaseEntity")>
import ${pkg};
    </#if>
</#list>
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import ${package.Entity}.${entity};
<#if entityLombokModel>
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
</#if>
import ${projectPackage}.model.vo.BaseVO;

/**
 * <p>
 * ${table.comment!} VO
 * </p>
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @date ${date}
 */
<#if entityLombokModel>
@Data
@AllArgsConstructor
@NoArgsConstructor
    <#if superEntityClass?? || activeRecord>
@EqualsAndHashCode(callSuper = true)
    </#if>
</#if>
<#if springdoc>
@Schema(description = "${table.comment!}")
<#elseif swagger>
@ApiModel(description = "${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${vo} extends BaseVO<${entity}, ${vo}><#if entitySerialVersionUID> implements Serializable</#if> {
<#elseif entitySerialVersionUID>
public class ${vo} implements Serializable {
<#else>
public class ${vo} {
</#if>
<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if !field.logicDeleteField>
        <#if field.comment!?length gt 0>
            <#if springdoc>
    @Schema(description = "${field.comment}")
            <#elseif swagger>
    @ApiModelProperty("${field.comment}")
            <#else>
    /**
     * ${field.comment}
     */
            </#if>
        </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>

    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

    <#if chainModel>
    public ${vo} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if chainModel>
        return this;
        </#if>
    }
    </#list>
</#if>
<#if !entityLombokModel>

    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
            "${field.propertyName} = " + ${field.propertyName} +
        <#else>
            ", ${field.propertyName} = " + ${field.propertyName} +
        </#if>
    </#list>
        "}";
    }
</#if>
}
