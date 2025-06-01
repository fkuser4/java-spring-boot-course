package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.model.RefreshToken;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.repository.RefreshTokenRepository;
import hr.tvz.kuser.njamapp.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserInfoRepository userInfoRepository;

    public RefreshToken createRefreshToken(String username) {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {

            throw new UsernameNotFoundException("Korisnik nije pronađen: " + username);
        }

        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserInfo(userInfo);

        if (existingTokenOpt.isPresent()) {
            RefreshToken refreshToken = existingTokenOpt.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(90000L));
            return refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .userInfo(userInfo)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(90000L))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Osvježavajući token je istekao. Molimo prijavite se ponovno!");
        }
        return token;
    }

    public void deleteByUsername(String username) {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) {
            refreshTokenRepository.findByUserInfo(userInfo)
                    .ifPresent(refreshTokenRepository::delete);
        }
    }
}