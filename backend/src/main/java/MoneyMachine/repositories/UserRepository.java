package MoneyMachine.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {
    User findByEmail(String username);
}
