package cn.cutepikachu.datawisemaster.model.dto.user;

import cn.cutepikachu.datawisemaster.common.PageRequest;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    @Min(1)
    private Long id;

    @Length(min = 4, max = 16)
    private String userAccount;

    @Length(min = 4, max = 20)
    private String userNickname;

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
