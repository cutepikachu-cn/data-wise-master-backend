package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "用户创建请求")
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户账户")
    @NotBlank
    @Pattern(regexp = "^[\\w-]{4,16}$")
    private String userAccount;
    @ApiModelProperty("用户昵称")
    @Length(min = 4, max = 20)
    private String userNickname;
    @ApiModelProperty("用户头像连接")
    @Length(max = 1024)
    private String userAvatar;
    @ApiModelProperty("用户简介")
    @Length(max = 512)
    private String userProfile;
    @ApiModelProperty("用户角色")
    @NotNull
    private UserRole userRole;

}
