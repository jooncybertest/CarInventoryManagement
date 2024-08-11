
package com.junsoo.project.carinventorymanagement.repository;

import com.junsoo.project.carinventorymanagement.entity.Car;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByUserEmail(String email);
    @Modifying
    @Transactional
    @Query("delete from Car c where c.id in :ids and c.user.email = :email")
    void deleteAllByIds(@Param("ids") List<Integer> ids, @Param("email") String email);

}
