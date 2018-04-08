package TorJava;

public interface OnHiddenServiceStatusChange {
	void onHiddenServiceStatusChange(String url, int newStatusPercent);
}
