package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.util.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.service.util.mapper.OrderMapper;
import ru.clevertec.ecl.service.util.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRep;
    private final UserServiceImpl userService;
    private final GiftCertificateServiceImpl gcService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final GiftCertificateMapper gcMapper;

    @Override
    public List<OrderResponse> findAll(EntityFilter filter, Pageable pageable) {
        return orderRep.findAll(Example.of(orderMapper.toFrom((OrderFilter) filter)), pageable)
                .stream().map(orderMapper::toFrom).toList();
    }

    @Override
    public OrderResponse findById(Long id) {
        return orderMapper.toFrom(orderRep.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Order with id = %d not found", id))));
    }

    @Override
    @Transactional
    public OrderResponse save(OrderRequest order) {
        Long userId = order.userId();
        Long gcId = order.gcId();
        GiftCertificate gc = gcMapper.toFrom(gcService.findById(gcId));
        if (Objects.isNull(gc)) {
            throw new ServiceException(String.format("Gift certificate with id = %d not found", gcId));
        }
        User user = userMapper.toFrom(userService.findById(userId));
        if (Objects.isNull(user)) {
            throw new ServiceException(String.format("User with id = %d not found", userId));
        }
        Order orderEntity = Order.builder()
                .cost(gc.getPrice())
                .date(LocalDateTime.now())
                .user(user)
                .gc(gc)
                .build();
        orderRep.save(orderEntity);
        user.addOrder(orderEntity);
        orderEntity.setGc(gc);
        return orderMapper.toFrom(orderEntity);
    }

    @Override
    public OrderResponse update(OrderRequest order) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Order> order = orderRep.findById(id);
        orderRep.delete(order.orElseThrow(
                () -> new ServiceException(String.format("Order with id = '%d' not found", id))));
    }

}