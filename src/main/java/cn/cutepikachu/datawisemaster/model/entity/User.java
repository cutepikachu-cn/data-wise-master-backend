package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "`user`", autoResultMap = true)
@Schema(name = "User", description = "用户表")
public class User extends BaseEntity<User, UserVO> implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户账户")
    @TableField("`user_account`")
    private String userAccount;

    @Schema(description = "用户密码")
    @TableField("`user_password`")
    private String userPassword;

    @Schema(description = "用户昵称")
    @TableField("`user_nickname`")
    private String userNickname;

    @Schema(description = "用户头像")
    @TableField("`user_avatar`")
    private String userAvatar;

    @Schema(description = "用户简介")
    @TableField("`user_profile`")
    private String userProfile;

    @Schema(description = "用户角色：user/admin/ban")
    @TableField("`user_role`")
    private String userRole;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "是否删除")
    @TableField("`is_delete`")
    @TableLogic
    private Byte isDelete;

    public static final String ID = "id";

    public static final String USER_ACCOUNT = "user_account";

    public static final String USER_PASSWORD = "user_password";

    public static final String USER_NICKNAME = "user_nickname";

    public static final String USER_AVATAR = "user_avatar";

    public static final String USER_PROFILE = "user_profile";

    public static final String USER_ROLE = "user_role";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";
}
