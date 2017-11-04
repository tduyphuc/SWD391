package model.repo;

import model.entity.PaymentType;

public interface IPaymentTypeRepo {
	public PaymentType getPaymentType(Integer id);
}
