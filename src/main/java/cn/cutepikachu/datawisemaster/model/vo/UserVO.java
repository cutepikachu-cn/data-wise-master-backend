package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户 VO
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 12:00:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BaseVO<User, UserVO> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户昵称")
    private String userNickname;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty("用户角色：user/admin/ban")
    private UserRole userRole;
}
