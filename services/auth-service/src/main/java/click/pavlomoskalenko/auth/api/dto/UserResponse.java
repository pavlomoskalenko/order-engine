package click.pavlomoskalenko.auth.api.dto;

import click.pavlomoskalenko.auth.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
