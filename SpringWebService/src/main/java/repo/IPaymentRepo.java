package repo;

import entity.Payment;

public interface IPaymentRepo {
	public void savePayment(Payment payment) throws Exception;
}
