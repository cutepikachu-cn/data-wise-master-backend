package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;
    @ApiModelProperty("用户昵称")
    private String userNickname;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty("用户角色：user/admin/ban")
    private UserRole userRole;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
