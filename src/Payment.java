
public abstract class Payment {

	PaymentMethods pm;

	public Payment(PaymentMethods pm) {

		this.pm = pm;
	}

	public abstract void pay();

}
