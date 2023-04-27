package icns.smartplantdashboardapi.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String name;
    private String phone;
   

    public JwtResponse(String token, Long id, String email, String name, String phone) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.name =name;
        this.phone = phone;
        
    }
}
