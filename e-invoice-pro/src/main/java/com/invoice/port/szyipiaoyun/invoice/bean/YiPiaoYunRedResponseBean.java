package com.invoice.port.szyipiaoyun.invoice.bean;

import java.util.List;

public class YiPiaoYunRedResponseBean {
    private String operateCode;
    private String message;
    private Datas datas;
    private String pageAndSort;
	
    public void setOperateCode(String operateCode) {
         this.operateCode = operateCode;
    }
    
    public String getOperateCode() {
         return operateCode;
    }

    public void setMessage(String message) {
         this.message = message;
    }
    
    public String getMessage() {
         return message;
    }

    public void setDatas(Datas datas) {
         this.datas = datas;
    }
    
    public Datas getDatas() {
         return datas;
    }

    public void setPageAndSort(String pageAndSort) {
         this.pageAndSort = pageAndSort;
    }
    
    public String getPageAndSort() {
         return pageAndSort;
    }
	 
	public class Datas {
		private long serialNo;
		private List<Details> details;
		private String projectName;
		private String taxTotal;
		private String noTaxTotal;
		private String priceTotal;
		private String invoiceCode;
		private String invoiceNum;
		private long invoiceDate;
		private String pdfUrl;
		private String invoiceType;
		private String reMarks;
		private String usrno;
		private String listFlag;
		private String listItemName;
		private String remainRedInvoices;
		private String serialNoReds;
		private String redPunchingAmount;
		private String todayDownloadCount;
		private String accDownloadCount;
		private String lastDownLoadDate;
		private String operatingType;
		private String emailStatus;
		private String usrname;
		private String status;
		private String variation;
		private String splitTo;
		private String shardFrom;
		private String annexFrom;
		private String mergeBy;
		private String notice;
		private String payStatus;
		private String checkCode;
		private String h5weburl;
		private String payStatusName;
		private String statusName;
		private String emailStatusName;
		private String invoiceTypeName;
	
		public void setSerialNo(long serialNo) {
			 this.serialNo = serialNo;
		}
		
		public long getSerialNo() {
			 return serialNo;
		}

		public void setDetails(List<Details> details) {
			 this.details = details;
		}
		
		public List<Details> getDetails() {
			 return details;
		}

		public void setProjectName(String projectName) {
			 this.projectName = projectName;
		}
		
		public String getProjectName() {
			 return projectName;
		}

		public void setTaxTotal(String taxTotal) {
			 this.taxTotal = taxTotal;
		}
		
		public String getTaxTotal() {
			 return taxTotal;
		}

		public void setNoTaxTotal(String noTaxTotal) {
			 this.noTaxTotal = noTaxTotal;
		}
		
		public String getNoTaxTotal() {
			 return noTaxTotal;
		}

		public void setPriceTotal(String priceTotal) {
			 this.priceTotal = priceTotal;
		}
		
		public String getPriceTotal() {
			 return priceTotal;
		}

		public void setInvoiceCode(String invoiceCode) {
			 this.invoiceCode = invoiceCode;
		}
		
		public String getInvoiceCode() {
			 return invoiceCode;
		}

		public void setInvoiceNum(String invoiceNum) {
			 this.invoiceNum = invoiceNum;
		}
		
		public String getInvoiceNum() {
			 return invoiceNum;
		}

		public void setInvoiceDate(long invoiceDate) {
			 this.invoiceDate = invoiceDate;
		}
		
		public long getInvoiceDate() {
			 return invoiceDate;
		}

		public void setPdfUrl(String pdfUrl) {
			 this.pdfUrl = pdfUrl;
		}
		
		public String getPdfUrl() {
			 return pdfUrl;
		}

		public void setInvoiceType(String invoiceType) {
			 this.invoiceType = invoiceType;
		}
		
		public String getInvoiceType() {
			 return invoiceType;
		}

		public void setReMarks(String reMarks) {
			 this.reMarks = reMarks;
		}
		
		public String getReMarks() {
			 return reMarks;
		}

		public void setUsrno(String usrno) {
			 this.usrno = usrno;
		}
		
		public String getUsrno() {
			 return usrno;
		}

		public void setListFlag(String listFlag) {
			 this.listFlag = listFlag;
		}
		
		public String getListFlag() {
			 return listFlag;
		}

		public void setListItemName(String listItemName) {
			 this.listItemName = listItemName;
		}
		
		public String getListItemName() {
			 return listItemName;
		}

		public void setRemainRedInvoices(String remainRedInvoices) {
			 this.remainRedInvoices = remainRedInvoices;
		}
		
		public String getRemainRedInvoices() {
			 return remainRedInvoices;
		}

		public void setSerialNoReds(String serialNoReds) {
			 this.serialNoReds = serialNoReds;
		}
		
		public String getSerialNoReds() {
			 return serialNoReds;
		}

		public void setRedPunchingAmount(String redPunchingAmount) {
			 this.redPunchingAmount = redPunchingAmount;
		}
		
		public String getRedPunchingAmount() {
			 return redPunchingAmount;
		}

		public void setTodayDownloadCount(String todayDownloadCount) {
			 this.todayDownloadCount = todayDownloadCount;
		}
		
		public String getTodayDownloadCount() {
			 return todayDownloadCount;
		}

		public void setAccDownloadCount(String accDownloadCount) {
			 this.accDownloadCount = accDownloadCount;
		}
		
		public String getAccDownloadCount() {
			 return accDownloadCount;
		}

		public void setLastDownLoadDate(String lastDownLoadDate) {
			 this.lastDownLoadDate = lastDownLoadDate;
		}
		
		public String getLastDownLoadDate() {
			 return lastDownLoadDate;
		}

		public void setOperatingType(String operatingType) {
			 this.operatingType = operatingType;
		}
		
		public String getOperatingType() {
			 return operatingType;
		}

		public void setEmailStatus(String emailStatus) {
			 this.emailStatus = emailStatus;
		}
		
		public String getEmailStatus() {
			 return emailStatus;
		}

		public void setUsrname(String usrname) {
			 this.usrname = usrname;
		}
		
		public String getUsrname() {
			 return usrname;
		}

		public void setStatus(String status) {
			 this.status = status;
		}
		
		public String getStatus() {
			 return status;
		}

		public void setVariation(String variation) {
			 this.variation = variation;
		}
		
		public String getVariation() {
			 return variation;
		}

		public void setSplitTo(String splitTo) {
			 this.splitTo = splitTo;
		}
		
		public String getSplitTo() {
			 return splitTo;
		}

		public void setShardFrom(String shardFrom) {
			 this.shardFrom = shardFrom;
		}
		
		public String getShardFrom() {
			 return shardFrom;
		}

		public void setAnnexFrom(String annexFrom) {
			 this.annexFrom = annexFrom;
		}
		
		public String getAnnexFrom() {
			 return annexFrom;
		}

		public void setMergeBy(String mergeBy) {
			 this.mergeBy = mergeBy;
		}
		
		public String getMergeBy() {
			 return mergeBy;
		}

		public void setNotice(String notice) {
			 this.notice = notice;
		}
		
		public String getNotice() {
			 return notice;
		}

		public void setPayStatus(String payStatus) {
			 this.payStatus = payStatus;
		}
		
		public String getPayStatus() {
			 return payStatus;
		}
		
		public void setCheckCode(String checkCode) {
			 this.checkCode = checkCode;
		}
		
		public String getCheckCode() {
			 return checkCode;
		}

		public void setH5weburl(String h5weburl) {
			 this.h5weburl = h5weburl;
		}
		
		public String getH5weburl() {
			 return h5weburl;
		}

		public void setPayStatusName(String payStatusName) {
			 this.payStatusName = payStatusName;
		}
		
		public String getPayStatusName() {
			 return payStatusName;
		}

		public void setStatusName(String statusName) {
			 this.statusName = statusName;
		}
		
		public String getStatusName() {
			 return statusName;
		}

		public void setEmailStatusName(String emailStatusName) {
			 this.emailStatusName = emailStatusName;
		}
		
		public String getEmailStatusName() {
			 return emailStatusName;
		}

		public void setInvoiceTypeName(String invoiceTypeName) {
			 this.invoiceTypeName = invoiceTypeName;
		}
		
		public String getInvoiceTypeName() {
			 return invoiceTypeName;
		}
		 
		public class Details {
			private String objectId;
			private String dataMark;
			private String rowNum;
			private String itemName;
			private String itemUnit;
			private String itemNum;
			private String specMode;
			private String itemPrice;
			private String taxIncluded;
			private int invoiceNature;
			private String itemTaxCode;
			private String yhzcbs;
			private String lslbs;
			private String zzstsgl;
			private String taxRate;
			private String amount;
			private String discountAmount;
			private String tax;
			private String discountLineObjId;
			
			public void setObjectId(String objectId) {
				 this.objectId = objectId;
			}
			
			public String getObjectId() {
				 return objectId;
			}

			public void setDataMark(String dataMark) {
				 this.dataMark = dataMark;
			}
			
			public String getDataMark() {
				 return dataMark;
			}

			public void setRowNum(String rowNum) {
				 this.rowNum = rowNum;
			}
			
			public String getRowNum() {
				 return rowNum;
			}

			public void setItemName(String itemName) {
				 this.itemName = itemName;
			}
			
			public String getItemName() {
				 return itemName;
			}

			public void setItemUnit(String itemUnit) {
				 this.itemUnit = itemUnit;
			}
			
			public String getItemUnit() {
				 return itemUnit;
			}

			public void setItemNum(String itemNum) {
				 this.itemNum = itemNum;
			}
			
			public String getItemNum() {
				 return itemNum;
			}

			public void setSpecMode(String specMode) {
				 this.specMode = specMode;
			}
			
			public String getSpecMode() {
				 return specMode;
			}

			public void setItemPrice(String itemPrice) {
				 this.itemPrice = itemPrice;
			}
			
			public String getItemPrice() {
				 return itemPrice;
			}

			public void setTaxIncluded(String taxIncluded) {
				 this.taxIncluded = taxIncluded;
			}
			
			public String getTaxIncluded() {
				 return taxIncluded;
			}

			public void setInvoiceNature(int invoiceNature) {
				 this.invoiceNature = invoiceNature;
			}
			
			public int getInvoiceNature() {
				 return invoiceNature;
			}

			public void setItemTaxCode(String itemTaxCode) {
				 this.itemTaxCode = itemTaxCode;
			}
			
			public String getItemTaxCode() {
				 return itemTaxCode;
			}

			public void setYhzcbs(String yhzcbs) {
				 this.yhzcbs = yhzcbs;
			}
			
			public String getYhzcbs() {
				 return yhzcbs;
			}

			public void setLslbs(String lslbs) {
				 this.lslbs = lslbs;
			}
			
			public String getLslbs() {
				 return lslbs;
			}

			public void setZzstsgl(String zzstsgl) {
				 this.zzstsgl = zzstsgl;
			}
			
			public String getZzstsgl() {
				 return zzstsgl;
			}

			public void setTaxRate(String taxRate) {
				 this.taxRate = taxRate;
			}
			
			public String getTaxRate() {
				 return taxRate;
			}

			public void setAmount(String amount) {
				 this.amount = amount;
			}
			
			public String getAmount() {
				 return amount;
			}

			public void setDiscountAmount(String discountAmount) {
				 this.discountAmount = discountAmount;
			}
			
			public String getDiscountAmount() {
				 return discountAmount;
			}

			public void setTax(String tax) {
				 this.tax = tax;
			}
			
			public String getTax() {
				 return tax;
			}

			public void setDiscountLineObjId(String discountLineObjId) {
				 this.discountLineObjId = discountLineObjId;
			}
			
			public String getDiscountLineObjId() {
				 return discountLineObjId;
			}
		}
	}
}
