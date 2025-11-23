package kr.domsam.youbankdummy.application;

import kr.domsam.youbankdummy.entity.CreditCardStatement;
import kr.domsam.youbankdummy.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardStatementRepository extends JpaRepository<CreditCardStatement, String> {
}
