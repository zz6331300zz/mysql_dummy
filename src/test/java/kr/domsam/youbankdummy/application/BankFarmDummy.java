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
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    CheckCardRepository checkCardRepository;

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
            Card assignedCard = cardList.get(faker.random().nextInt(cardList.size()));
            UserCard uc = generateUserCard(c,assignedCard,assignedEmp);
            userCardRepository.save(uc);
            if(assignedCard.getCardTp()==1) {
                insCreditCard(uc);
            }else if (assignedCard.getCardTp()==0){
                insCheckCard(uc);
            }
            empIdx = (empIdx + 1) % employeeList.size();
        }
        userCardRepository.flush();
    }


    void insCheckCard(UserCard uc) {
        Account a = accountList.get(faker.random().nextInt(accountList.size()));
        CheckCard checkCard = generateCheckCard(uc,a);
        checkCardRepository.save(checkCard);
        checkCardRepository.flush();
    }

    CheckCard generateCheckCard(UserCard uc, Account a) {
        return CheckCard.builder()
                .cardUserId(uc.getCardUserId())
                .account(a)
                .build();
    }

    void insCreditCard(UserCard uc) {
        CreditCard creditCard = generateCreditCard(uc);
        creditCardRepository.save(creditCard);
        creditCardRepository.flush();
    }

    CreditCard generateCreditCard(UserCard uc) {
        return CreditCard.builder()
                .cardUserId(uc.getCardUserId())
                .cardAccountId(generateAccountNo())
                .cardDueDay(generateDueDay())
                .cardBankCode("11111")
                .build();
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

    private static byte generateDueDay() {
        return (byte) faker.number().numberBetween(1, 31);
    }

    private static LocalDateTime randomDateFuture() {
        return LocalDateTime.now().plusDays(
                ThreadLocalRandom.current().nextInt(30, 3000)
        );
    }








}
