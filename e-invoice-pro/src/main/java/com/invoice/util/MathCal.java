package com.invoice.util;

import java.math.BigDecimal;

//本类用于精确计算

public class MathCal {
	
	 public static double add(double value1,double value2,int scale){
		 if(scale<0){         
	            throw new RuntimeException("精确度不能小于0");
	         }
         BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
         BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
         return b1.add(b2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
     }
	 
	 public static double sub(double value1,double value2,int scale){
		 if(scale<0){         
	            throw new RuntimeException("精确度不能小于0");
	         }
         BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
         BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
         return b1.subtract(b2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
     }
	 
	 public static double mul(double value1,double value2,int scale){
		 if(scale<0){         
	            throw new RuntimeException("精确度不能小于0");
	         }
         BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
         BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
         return b1.multiply(b2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
     }
	 public static double div(double value1,double value2,int scale) {
    //如果精确范围小于0，抛出异常信息
          if(scale<0){         
            throw new RuntimeException("精确度不能小于0");
         }
          BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
          BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
         return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();    
     }

	 public static double getScale(double value, int scale){
		
         if(scale<0){         
           throw new RuntimeException("精确度不能小于0");
        }
		 BigDecimal b = new BigDecimal(Double.valueOf(value));
		 return b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	 }
}
