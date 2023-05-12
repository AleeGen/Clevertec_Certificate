package ru.clevertec.ecl.util.builder.impl.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.util.builder.EntityBuilder;

import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserRes")
public class UserResBuilder implements EntityBuilder<UserResponse> {

    private Long id = 0L;
    private String firstname = "first";
    private String lastname = "last";
    private List<OrderResponse> orders = new ArrayList<>(List.of(
            OrderResBuilder.aOrderRes().withId(1L).build(),
            OrderResBuilder.aOrderRes().withId(2L).build(),
            OrderResBuilder.aOrderRes().withId(3L).build()));

    @Override
    public UserResponse build() {
        return UserResponse.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }

}