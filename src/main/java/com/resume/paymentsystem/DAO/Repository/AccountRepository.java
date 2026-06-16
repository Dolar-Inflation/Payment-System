package com.resume.paymentsystem.DAO.Repository;

import com.resume.paymentsystem.DAO.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("select a from Account a where a.name = :username")
    Account findByName(@Param("username")String name);

    boolean existsAccountsByName(String name);
}
