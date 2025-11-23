package wowa.myqna.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wowa.myqna.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
