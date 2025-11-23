package kr.domsam.youbankdummy.application;

import kr.domsam.youbankdummy.Dummy;
import kr.domsam.youbankdummy.entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankFarmDummy extends Dummy {
    @Autowired
    CustomerRepository2 customerRepository2;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    EmployeesRepository employeesRepository;
    @Autowired
    UserCardRepository userCardRepository;

    List<Customer2> customerList2;
    List<Account> accountList;
    List<Card> cardList;
    List<Employees> employeeList;

    @BeforeAll
    void beforeAll() {

        customerList2 = customerRepository2.findAll();
        accountList = accountRepository.findAll();
        cardList = cardRepository.findAll();
        employeeList = employeesRepository.findAll();
    }


    @Test
    @Rollback(false)
    void insOneByOne() {
        int empIdx = 0;
        int cardIdx = 0;
        for(Customer2 c : customerList2) {
            Employees assignedEmp = employeeList.get(empIdx);
            Card assignedCard = cardList.get(cardIdx);
            UserCard uc = generateUserCard(c,assignedCard,assignedEmp);
            userCardRepository.save(uc);
        }
        userCardRepository.flush();
    }

    UserCard generateUserCard(Customer2 customer2,
                              Card card,
                              Employees employees) {
        return UserCard.builder()
                .card(card)
                .employee(employees)
                .customer2(customer2)
                .cardNum(generateAccountNo())
                .cardSts("ACTIVE")
                .cardDayLimit(500000)
                .cardMonthLimit(20000000)
                .cardEdAt(randomDateFuture())
                .cardDeacAt(null)
                .build();


    }

    private static String generateAccountNo() {
        return faker.number().digits(16);
    }

    private static LocalDateTime randomDateFuture() {
        return LocalDateTime.now().plusDays(
                ThreadLocalRandom.current().nextInt(30, 3000)
        );
    }








}
