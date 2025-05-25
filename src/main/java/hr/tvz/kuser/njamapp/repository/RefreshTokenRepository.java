package hr.tvz.kuser.njamapp.repository;

import hr.tvz.kuser.njamapp.model.RefreshToken;
import hr.tvz.kuser.njamapp.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserInfo(UserInfo userInfo);
}
