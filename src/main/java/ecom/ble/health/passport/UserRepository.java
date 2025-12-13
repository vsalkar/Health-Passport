package ecom.ble.health.passport;

import ecom.ble.health.passport.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
