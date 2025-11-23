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
public class CardStatmentDummy extends Dummy {

    @Autowired
    UserCardRepository userCardRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CreditCardStatementRepository cardStatementRepository;
    @Autowired
    CardInstallmentScheduleRepository cardInstallmentScheduleRepository;

    List<UserCard> userCardList;
    List<Card> cardList;
    List<CreditCardStatement> crdCardStmList;

    @BeforeAll
    void beforeAll() {
        userCardList = userCardRepository.findAll();
        cardList = cardRepository.findAll();
        crdCardStmList = cardStatementRepository.findAll();
    }

    @Test
    @Rollback(false)
    void insOneByOne() {

        for( UserCard uc: userCardList){
            if(uc.getCard().getCardTp()==0){
                insCrdCardStm(uc);
                insInstmSchd();
            }
        }
    }
    void insInstmSchd(){
        for(CreditCardStatement cs:crdCardStmList){
            int monthNo = cs.getCardInstallments();
            if(cs.getCardCrdRefundYn().equals("N")) {
                for (int i = 1; i <= monthNo; i++) {
                    CardInstallmentSchedule cis = generateCis(cs,i);
                    cardInstallmentScheduleRepository.save(cis);

                }
            }

        }
        cardInstallmentScheduleRepository.flush();
    }

    CardInstallmentSchedule generateCis(CreditCardStatement cs,int n){
        int istmAmt= cs.getCardOgAmt() / cs.getCardInstallments();
        return CardInstallmentSchedule.builder()
                .creditCardStatement(cs)
                .cardMonthNo(n)
                .cardInstallmentAmt(istmAmt)
                .cardDueAt(randomDateFuture())
                .cardScheduleRefundYn("N")
                .build();
    }

    void insCrdCardStm(UserCard uc) {

        CreditCardStatement crdCardStm = generateCrdCardStm(uc);
        cardStatementRepository.save(crdCardStm);
        cardStatementRepository.flush();
    }

    CreditCardStatement generateCrdCardStm(UserCard uc){
        return CreditCardStatement.builder()
                .userCard(uc)
                .cardPlace(faker.options().option("스타벅스","롯데리아","버거킹","피자헛"))
                .cardCrdRefundYn("N")
                .cardOgAmt(faker.options().option(100000,300000,600000,1000000))
                .cardInstallments(faker.options().option(3,6,10,12))
                .cardTrnsDt(randomDatePast())
                .build();
    }




    private static LocalDateTime randomDatePast() {
        return LocalDateTime.now().minusDays(
                ThreadLocalRandom.current().nextInt(30, 3000)
        );
    }


    private static LocalDateTime randomDateFuture() {
        return LocalDateTime.now().plusDays(
                ThreadLocalRandom.current().nextInt(30, 3000)
        );
    }


    }
