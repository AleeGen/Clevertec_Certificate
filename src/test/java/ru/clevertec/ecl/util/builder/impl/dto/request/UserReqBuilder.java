package ru.clevertec.ecl.util.builder.impl.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.util.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserReq")
public class UserReqBuilder implements EntityBuilder<UserRequest> {

    private Long id = 1L;
    private String firstname = "first";
    private String lastname = "last";

    @Override
    public UserRequest build() {
        return UserRequest.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }

}