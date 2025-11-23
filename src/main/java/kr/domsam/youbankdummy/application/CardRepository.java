package kr.domsam.youbankdummy.application;

import kr.domsam.youbankdummy.entity.BankAccount;
import kr.domsam.youbankdummy.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card, String> {
}
