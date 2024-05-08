package cn.cutepikachu.datawisemaster.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "用户登录请求")
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户账户")
    @NotBlank
    @Pattern(regexp = "^[\\w-]{4,16}$")
    private String userAccount;
    @ApiModelProperty("用户密码")
    @NotBlank
    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String userPassword;
}
