package cn.cutepikachu.datawisemaster.model.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UserUpdateSelfRequest implements Serializable {

    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String userPassword;

    @Length(min = 4, max = 20)
    private String userNickname;

    @Length(max = 1024)
    private String userAvatar;

    @Length(max = 512)
    private String userProfile;

    @Serial
    private static final long serialVersionUID = 1L;
}
