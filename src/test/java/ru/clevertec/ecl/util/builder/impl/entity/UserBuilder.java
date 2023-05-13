package ru.clevertec.ecl.util.builder.impl.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.util.builder.EntityBuilder;

import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
public class UserBuilder implements EntityBuilder<User> {

    private Long id = 1L;
    private String firstname = "first";
    private String lastname = "last";
    private List<Order> orders = new ArrayList<>(List.of(
            OrderBuilder.aOrder().withId(1L).build(),
            OrderBuilder.aOrder().withId(2L).build(),
            OrderBuilder.aOrder().withId(3L).build()));

    @Override
    public User build() {
        return User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .orders(orders)
                .build();
    }

}