package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UserUpdateRequest implements Serializable {

    @NotNull
    @Min(1)
    private Long id;

    @Length(min = 4, max = 20)
    private String userNickname;

    @Length(max = 1024)
    private String userAvatar;

    @Length(max = 512)
    private String userProfile;

    private String userRole;

    @AssertTrue
    boolean isValidUserRole() {
        return userRole == null || UserRole.getEnumByValue(userRole) != null;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
