package data.repo;

import data.entity.Payment;

public interface IPaymentRepo {
	public void savePayment(Payment payment) throws Exception;
}
