package wowa.myqna.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wowa.myqna.global.exception.ApplicationCustomException;
import wowa.myqna.global.message.ErrorMessage;
import wowa.myqna.user.domain.UserEntity;
import wowa.myqna.user.repository.UserRepository;

import static wowa.myqna.global.message.ErrorMessage.*;

@Transactional
@Service
@RequiredArgsConstructor
public class UserLowService {

    private final UserRepository userRepository;

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findById(String id) {
        return userRepository.findById(id).orElseThrow(()-> new ApplicationCustomException(NOT_FOUND_USER));
    }
}
