package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;
import ru.clevertec.ecl.dto.request.filter.impl.UserFilter;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.service.util.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRep;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> findAll(EntityFilter filter, Pageable pageable) {
        return userRep.findAll(Example.of(mapper.toFrom((UserFilter) filter)), pageable)
                .stream().map(mapper::toFrom).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        return mapper.toFrom(userRep.findById(id).orElseThrow(() ->
                new ServiceException(String.format("User with id = %d not found", id))));
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest user) {
        return mapper.toFrom(userRep.save(mapper.toFrom(user)));

    }

    @Override
    @Transactional
    public UserResponse update(UserRequest user) {
        User u = userRep.findById(user.id()).orElseThrow(() ->
                new ServiceException(String.format("User with id = %d not found", user.id())));
        mapper.updateUser(u, user);
        return mapper.toFrom(u);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> user = userRep.findById(id);
        userRep.delete(user.orElseThrow(
                () -> new ServiceException(String.format("User with id = '%d' not found", id))));
    }

}