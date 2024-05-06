package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
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
public class UserAddRequest implements Serializable {

    @NotBlank
    @Pattern(regexp = "^[\\w-]{4,16}$")
    private String userAccount;

    @Length(min = 4, max = 20)
    private String userNickname;

    @Length(max = 1024)
    private String userAvatar;

    @Length(max = 512)
    private String userProfile;

    private String userRole = UserRole.USER.getValue();

    @AssertTrue
    boolean isValidUserRole() {
        return userRole == null || UserRole.getEnumByValue(userRole) != null;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
