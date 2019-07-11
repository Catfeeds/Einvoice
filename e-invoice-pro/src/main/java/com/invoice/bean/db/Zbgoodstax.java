package com.invoice.bean.db;

public class Zbgoodstax {
    private String goodsBarcode;
    private String goodsId;
    private String goodsName;
    private String unit;
    private Double taxrate;
    private String categateId;
    private String taxitemid;
    private String entid;

    public String getEntid() {
		return entid;
	}

	public void setEntid(String entid) {
		this.entid = entid;
	}

	public String getTaxitemid() {
		return taxitemid;
	}

	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
	}

	public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }

    public String getCategateId() {
        return categateId;
    }

    public void setCategateId(String categateId) {
        this.categateId = categateId;
    }

    
}
