package kr.domsam.youbankdummy.application;

import kr.domsam.youbankdummy.entity.CardInstallmentSchedule;
import kr.domsam.youbankdummy.entity.CreditCardStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardInstallmentScheduleRepository extends JpaRepository<CardInstallmentSchedule, String> {
}
