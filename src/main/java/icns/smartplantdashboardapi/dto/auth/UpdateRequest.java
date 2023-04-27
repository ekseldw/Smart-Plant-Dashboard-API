package icns.smartplantdashboardapi.dto.auth;

import icns.smartplantdashboardapi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRequest {
    private Long id;

    private String password;

    private String phone;

    private String name;

    public User toEntity() {
        return User.builder()
                .password(password)
                .phone(phone)
                .name(name)
                .build();
    }
}
