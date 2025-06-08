package co.za.zwibvafhi.livestock.persistence.repository;

import co.za.zwibvafhi.livestock.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing user entities.
 *
 * <p>Provides CRUD operations for {@link User} to handle user data persistence.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}