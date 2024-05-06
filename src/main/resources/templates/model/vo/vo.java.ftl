package ${package.Parent}.model.vo;

<#list table.importPackages as pkg>
    <#if !pkg?contains("mybatisplus") && !pkg?contains("BaseEntity")>
import ${pkg};
    </#if>
</#list>
import ${package.Entity}.${entity};
<#if entityLombokModel>
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
</#if>
<#if entitySerialVersionUID>
import java.io.Serial;
</#if>
import ${projectPackage}.model.vo.BaseVO;

/**
 * <p>
 * ${table.comment!} VO
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
@AllArgsConstructor
@NoArgsConstructor
    <#if superEntityClass?? || activeRecord>
@EqualsAndHashCode(callSuper = true)
    </#if>
</#if>
<#if superEntityClass??>
public class ${entity}VO extends BaseVO<${entity}, ${entity}VO><#if entitySerialVersionUID> implements Serializable</#if> {
<#elseif entitySerialVersionUID>
public class ${entity}VO implements Serializable {
<#else>
public class ${entity}VO {
</#if>
<#if entitySerialVersionUID>
    @Serial
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
     * ${field.comment}
     */
    private ${field.propertyType} ${field.propertyName};
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
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
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
