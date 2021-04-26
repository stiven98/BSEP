package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.bsep.domain.users.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select s from users s where s.username= ?1")
    User findUserByEmail(String email);

    User findByActivationId(UUID activationId);
}