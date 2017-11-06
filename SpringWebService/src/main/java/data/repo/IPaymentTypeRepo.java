package data.repo;

import data.entity.PaymentType;

public interface IPaymentTypeRepo {
	public PaymentType getPaymentType(Integer id);
}
