package cn.cutepikachu.datawisemaster.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class LoginUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String userNickname;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户角色：user/admin
     */
    private String userRole;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
