package ru.clevertec.ecl.data.builder.impl.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.data.builder.EntityBuilder;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrder")
public class OrderBuilder implements EntityBuilder<Order> {

    private Long id = 1L;
    private Double cost = 1.0;
    private LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private User user = User.builder().id(1L).build();
    private GiftCertificate gc = GiftCertificate.builder().id(1L).build();

    @Override
    public Order build() {
        return Order.builder()
                .id(id)
                .cost(cost)
                .date(date)
                .user(user)
                .gc(gc)
                .build();
    }

}