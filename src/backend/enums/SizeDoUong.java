package backend.enums;

public enum SizeDoUong {
    S(0),      // Size S k cộng thêm tiền
    M(5000),   // Size M cộng 5k
    L(10000);  // Size L cộng 10k

    private final double phuPhi;

    SizeDoUong(double phuPhi) {
        this.phuPhi = phuPhi;
    }

    public double getPhuPhi() { return phuPhi; }
}