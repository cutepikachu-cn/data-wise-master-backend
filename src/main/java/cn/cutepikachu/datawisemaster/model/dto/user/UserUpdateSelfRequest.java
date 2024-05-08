package cn.cutepikachu.datawisemaster.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "用户更新个人信息请求")
public class UserUpdateSelfRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户密码")
    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String userPassword;
    @ApiModelProperty("用户昵称")
    @Length(min = 4, max = 20)
    private String userNickname;
    @ApiModelProperty("用户头像链接")
    @Length(min = 1, max = 1024)
    private String userAvatar;
    @ApiModelProperty("用户简介")
    @Length(min = 1, max = 512)
    private String userProfile;

}
