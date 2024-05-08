package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.common.PageRequest;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserQueryRequest", description = "用户查询请求")
public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    @Min(1)
    private Long id;
    @ApiModelProperty("用户账户")
    @Length(min = 1, max = 16)
    private String userAccount;
    @ApiModelProperty("用户昵称")
    @Length(min = 1, max = 20)
    private String userNickname;
    @ApiModelProperty("用户简介")
    @Length(min = 1, max = 512)
    private String userProfile;
    @ApiModelProperty("用户角色")
    private UserRole userRole;
}
