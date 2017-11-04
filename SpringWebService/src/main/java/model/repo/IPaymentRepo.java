package model.repo;

import model.entity.Payment;

public interface IPaymentRepo {
	public void savePayment(Payment payment) throws Exception;
}
