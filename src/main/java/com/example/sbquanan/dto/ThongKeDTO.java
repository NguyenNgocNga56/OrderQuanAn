package com.example.sbquanan.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDTO {
    private long tongDon;
    private long donHoanThanh;
    private long donDaHuy;
    private double doanhThu;       
    private long soKhach;          
}