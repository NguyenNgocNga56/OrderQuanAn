package com.example.sbquanan.enums;

public enum SizeDoUong {
    S(0),
    M(5000),
    L(10000);

    private final double phuPhi;
    SizeDoUong(double phuPhi) { this.phuPhi = phuPhi; }
    public double getPhuPhi() { return phuPhi; }
}
