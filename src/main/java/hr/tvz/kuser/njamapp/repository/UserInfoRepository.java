package hr.tvz.kuser.njamapp.repository;

import hr.tvz.kuser.njamapp.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
}
