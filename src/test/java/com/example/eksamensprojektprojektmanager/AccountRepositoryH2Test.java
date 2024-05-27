package com.example.eksamensprojektprojektmanager;


import com.example.eksamensprojektprojektmanager.model.Account;
import com.example.eksamensprojektprojektmanager.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
public class AccountRepositoryH2Test {


    @Autowired
    AccountRepository repository;


    @Test
    void findAccount(){
        Account found = repository.getUserById(1L);
        assertEquals("testUser1",found.getUsername());
    }

    @Test
    void createUser() {

        Account newAccount = new Account();
        newAccount.setUsername("testUser3");
        newAccount.setPassword("testPassword3");
        newAccount.setAdmin(true);
        /*
        newAccount.setName(null);
        newAccount.setCompany(null);
        newAccount.setJobTitle(null);
        newAccount.setDescription(null);
*/
        repository.save(newAccount);

        Account retrievedAccount = repository.getUserById(3L);

        assertEquals("testUser3", retrievedAccount.getUsername());
    }

    @Test
    void loginTest() {
        boolean isValidUser = repository.isValidUser("testUser1", "testPassword1");

        assertTrue(isValidUser);
    }
    @Test
    void loginFailedTest() {
        String username = "nonExistingUser";
        String password = "nonExistingPassword";

        boolean isValidUser = repository.isValidUser(username, password);

        assertFalse(isValidUser);
    }

}
