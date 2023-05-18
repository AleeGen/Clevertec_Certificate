package ru.clevertec.ecl.data.builder.impl.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.data.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserReq")
public class UserReqBuilder implements EntityBuilder<UserRequest> {

    private Long id = 1L;
    private String firstname = "firstname";
    private String lastname = "lastname";

    @Override
    public UserRequest build() {
        return UserRequest.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }

}