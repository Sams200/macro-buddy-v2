package com.example.sams.repo;

import com.example.sams.entity.Entry;
import com.example.sams.entity.Food;
import com.example.sams.entity.User;
import com.example.sams.enumeration.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class EntryRepoTest {
    @Autowired
    EntryRepo entryRepo;

    private Food food;

    private User user1;
    private User user2;

    private Entry entry1;
    private Entry entry2;
    private Entry entry3;
    @Autowired
    private FoodRepo foodRepo;
    @Autowired
    private UserRepo userRepo;

    private Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        food = Food.builder()
                .name("TestFood1")
                .producer("Mega")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(265)
                .build();

        user1= User.builder()
                .username("User1")
                .password("password1")
                .email("email1")
                .role(Role.ROLE_USER)
                .build();

        user2= User.builder()
                .username("User2")
                .password("password2")
                .email("email2")
                .role(Role.ROLE_USER)
                .build();

        entry1=Entry.builder()
                .date(LocalDate.now())
                .food(food)
                .quantity(1f)
                .user(user1)
                .build();

        entry2=Entry.builder()
                .date(LocalDate.of(2020,1,1))
                .food(food)
                .quantity(2f)
                .user(user1)
                .build();

        entry3=Entry.builder()
                .date(LocalDate.now())
                .food(food)
                .quantity(3f)
                .user(user2)
                .build();

        food = foodRepo.save(food);

        user1 = userRepo.save(user1);
        user2 = userRepo.save(user2);

        entryRepo.save(entry1);
        entryRepo.save(entry2);
        entryRepo.save(entry3);
    }

    @Test
    void findByUserId_ShouldReturnMatchingEntries() {
        // Arrange

        // Act
        List<Entry> result=entryRepo.findByUser_UserId(user1.getUserId());

        // Assert
        assertEquals(2, result.size());
        for(Entry entry:result){
            System.out.println(entry.getDate());
        }
    }

    @Test
    void findByUser_UserIdAndDate_ShouldReturnMatchingEntries() {
        // Arrange

        // Act
        List<Entry> result=entryRepo.findByUser_UserIdAndDate(user1.getUserId(),LocalDate.now());

        // Assert
        assertEquals(1, result.size());
    }
}
