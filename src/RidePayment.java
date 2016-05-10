

public class RidePayment extends Payment {
	int value;

	public RidePayment(PaymentMethods pm) {
		super(pm);

	}

	@Override
	public void pay() {
		pm.processPayment();

	}

}
