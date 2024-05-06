package cn.cutepikachu.datawisemaster.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {

    @NotBlank
    @Pattern(regexp = "^[\\w-]{4,16}$")
    private String userAccount;

    @NotBlank
    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String userPassword;

    @Serial
    private static final long serialVersionUID = 1L;
}
