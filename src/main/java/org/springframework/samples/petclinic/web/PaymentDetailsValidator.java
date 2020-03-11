
package org.springframework.samples.petclinic.web;

import java.time.YearMonth;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PaymentDetailsValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return Owner.class.isAssignableFrom(clazz);
	}

	public boolean checkCard(final String card) {
		int sum = 0;
		boolean alternate = false;
		for (int i = card.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(card.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = n % 10 + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return sum % 10 == 0;
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Owner owner = (Owner) target;

		if (owner.getCreditCardNumber() != "" && owner.getExpirationMonth() != null && owner.getExpirationYear() != null && owner.getCvv() != null) {

			String cvv = owner.getCvv();
			Integer expMonth = owner.getExpirationMonth();
			Integer expYear = owner.getExpirationYear();
			String username = owner.getUser().getUsername();

			if (!this.checkCard(owner.getCreditCardNumber())) {
				errors.rejectValue("creditCardNumber", "Credit Card Number is not correct", "Credit Card Number is not correct");
			}

			if (!cvv.matches("^[0-9]*$")) {
				errors.rejectValue("cvv", "CVV must only contains numbers", "CVV must only contains numbers");
			} else {

				if (cvv.length() != 3) {
					errors.rejectValue("cvv", "CVV must have 3 numbers", "CVV must have 3 numbers");
				}
			}

			if (expMonth < 1 || expMonth > 12) {
				errors.rejectValue("expirationMonth", "The Month must be between 1 and 12", "The Month must be between 1 and 12");
			}

			if (expYear < 0) {
				errors.rejectValue("expirationYear", "The Year must be positive", "The Year must be positive");
			}

			if (!(expMonth < 1 || expMonth > 12) && expYear > 0) {
				YearMonth expDate = YearMonth.of(expYear, expMonth);
				YearMonth today = YearMonth.now();
				if (today.isAfter(expDate)) {
					errors.rejectValue("expirationYear", "The Credit Card you introduced has expired", "The Credit Card you introduced has expired");
				}
			}

		} else {
			errors.rejectValue("expirationYear", "You must fill all the fields", "You must fill all the fields");
		}

	}

}
