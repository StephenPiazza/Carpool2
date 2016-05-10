

public class RidePayment extends Payment {
	int value;

	public RidePayment(PaymentMethods pm) {
		super(pm);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void pay() {
		pm.processPayment();

	}

}
