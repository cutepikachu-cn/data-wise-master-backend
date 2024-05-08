package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "用户更新请求")
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    @NotNull
    @Min(1)
    private Long id;
    @ApiModelProperty("用户昵称")
    @Length(min = 4, max = 20)
    private String userNickname;
    @ApiModelProperty("用户头像链接")
    @Length(min = 1, max = 1024)
    private String userAvatar;
    @ApiModelProperty("用户简介")
    @Length(min = 1, max = 512)
    private String userProfile;
    @ApiModelProperty("用户角色")
    private UserRole userRole;

}
