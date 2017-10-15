package blog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.Role;
/**
 * Created by user on 14.8.2017 г..
 */
public interface RoleRepository extends JpaRepository<Role,Integer>{
    Role findByName(String name);
}
