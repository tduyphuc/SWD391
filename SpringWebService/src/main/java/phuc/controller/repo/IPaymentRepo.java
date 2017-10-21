package phuc.controller.repo;

import phuc.entity.Payment;

public interface IPaymentRepo {
	public void savePayment(Payment payment) throws Exception;
}
