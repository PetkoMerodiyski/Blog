package blog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.User;
/**
 * Created by user on 14.8.2017 г..
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
