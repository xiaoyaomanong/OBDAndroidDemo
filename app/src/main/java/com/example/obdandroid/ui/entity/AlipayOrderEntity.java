package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/4/9 0009
 * 描述：
 */
public class AlipayOrderEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"order_no":"86a3e8e9db71467dab86f9a08ae72e27","orderStr":{"code":null,"msg":null,"subCode":null,"subMsg":null,"body":"alipay_sdk=alipay-sdk-java-4.9.79.ALL&app_id=2021002125608234&biz_content=%7B%22body%22%3A%2210%E6%AC%A119.9%22%2C%22out_trade_no%22%3A%2286a3e8e9db71467dab86f9a08ae72e27%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2210%E6%AC%A119.9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxzbpkw.natappfree.cc%2Fapi%2Falipay&sign=edRmBsrpZmc79lw%2B6NR%2FRVFVWhjOccH9kKkU8GVa%2BTvjBX12rvlCD8NL%2Fs4HTCDl%2B%2F%2F4cqJxEkCDOpydM4xmPkzcGtlpGm%2FgtvKeSWNgW0zDshW7wnbfgQJil%2But5Q01i%2BtDBMr4%2BXMWYDDYV3v8TuBY0jVkVhujTH1MuF0WN9mazl5qprTqknEVa8F9i00jNy7%2FtNdvIeVjcZn9aom1MREqjWhMrfgQKV1Xwq%2ByZwxOTQyHMMcH%2BAJy%2Fq%2B7srGLsI8GNDqgPA51wlR%2Fu90IQ%2BpR07i62VHwg1sMuRsWlKNcbpiscAjhbocoFO66IHS9B%2BwQFbX0wO%2BJqcxL03mbYg%3D%3D&sign_type=RSA2&timestamp=2021-04-09+17%3A30%3A15&version=1.0","params":null,"merchantOrderNo":null,"outTradeNo":null,"sellerId":null,"totalAmount":null,"tradeNo":null,"errorCode":null,"success":true},"total_fee":0.01}
     */

    private boolean success;
    private String code;
    private String message;
    private DataEntity data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * order_no : 86a3e8e9db71467dab86f9a08ae72e27
         * orderStr : {"code":null,"msg":null,"subCode":null,"subMsg":null,"body":"alipay_sdk=alipay-sdk-java-4.9.79.ALL&app_id=2021002125608234&biz_content=%7B%22body%22%3A%2210%E6%AC%A119.9%22%2C%22out_trade_no%22%3A%2286a3e8e9db71467dab86f9a08ae72e27%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2210%E6%AC%A119.9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxzbpkw.natappfree.cc%2Fapi%2Falipay&sign=edRmBsrpZmc79lw%2B6NR%2FRVFVWhjOccH9kKkU8GVa%2BTvjBX12rvlCD8NL%2Fs4HTCDl%2B%2F%2F4cqJxEkCDOpydM4xmPkzcGtlpGm%2FgtvKeSWNgW0zDshW7wnbfgQJil%2But5Q01i%2BtDBMr4%2BXMWYDDYV3v8TuBY0jVkVhujTH1MuF0WN9mazl5qprTqknEVa8F9i00jNy7%2FtNdvIeVjcZn9aom1MREqjWhMrfgQKV1Xwq%2ByZwxOTQyHMMcH%2BAJy%2Fq%2B7srGLsI8GNDqgPA51wlR%2Fu90IQ%2BpR07i62VHwg1sMuRsWlKNcbpiscAjhbocoFO66IHS9B%2BwQFbX0wO%2BJqcxL03mbYg%3D%3D&sign_type=RSA2&timestamp=2021-04-09+17%3A30%3A15&version=1.0","params":null,"merchantOrderNo":null,"outTradeNo":null,"sellerId":null,"totalAmount":null,"tradeNo":null,"errorCode":null,"success":true}
         * total_fee : 0.01
         */

        private String order_no;
        private OrderStrEntity orderStr;
        private double total_fee;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public OrderStrEntity getOrderStr() {
            return orderStr;
        }

        public void setOrderStr(OrderStrEntity orderStr) {
            this.orderStr = orderStr;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }

        public static class OrderStrEntity {
            /**
             * code : null
             * msg : null
             * subCode : null
             * subMsg : null
             * body : alipay_sdk=alipay-sdk-java-4.9.79.ALL&app_id=2021002125608234&biz_content=%7B%22body%22%3A%2210%E6%AC%A119.9%22%2C%22out_trade_no%22%3A%2286a3e8e9db71467dab86f9a08ae72e27%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2210%E6%AC%A119.9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxzbpkw.natappfree.cc%2Fapi%2Falipay&sign=edRmBsrpZmc79lw%2B6NR%2FRVFVWhjOccH9kKkU8GVa%2BTvjBX12rvlCD8NL%2Fs4HTCDl%2B%2F%2F4cqJxEkCDOpydM4xmPkzcGtlpGm%2FgtvKeSWNgW0zDshW7wnbfgQJil%2But5Q01i%2BtDBMr4%2BXMWYDDYV3v8TuBY0jVkVhujTH1MuF0WN9mazl5qprTqknEVa8F9i00jNy7%2FtNdvIeVjcZn9aom1MREqjWhMrfgQKV1Xwq%2ByZwxOTQyHMMcH%2BAJy%2Fq%2B7srGLsI8GNDqgPA51wlR%2Fu90IQ%2BpR07i62VHwg1sMuRsWlKNcbpiscAjhbocoFO66IHS9B%2BwQFbX0wO%2BJqcxL03mbYg%3D%3D&sign_type=RSA2&timestamp=2021-04-09+17%3A30%3A15&version=1.0
             * params : null
             * merchantOrderNo : null
             * outTradeNo : null
             * sellerId : null
             * totalAmount : null
             * tradeNo : null
             * errorCode : null
             * success : true
             */

            private String code;
            private String msg;
            private String subCode;
            private String subMsg;
            private String body;
            private String params;
            private String merchantOrderNo;
            private String outTradeNo;
            private String sellerId;
            private String totalAmount;
            private String tradeNo;
            private String errorCode;
            private boolean success;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getSubCode() {
                return subCode;
            }

            public void setSubCode(String subCode) {
                this.subCode = subCode;
            }

            public String getSubMsg() {
                return subMsg;
            }

            public void setSubMsg(String subMsg) {
                this.subMsg = subMsg;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getParams() {
                return params;
            }

            public void setParams(String params) {
                this.params = params;
            }

            public String getMerchantOrderNo() {
                return merchantOrderNo;
            }

            public void setMerchantOrderNo(String merchantOrderNo) {
                this.merchantOrderNo = merchantOrderNo;
            }

            public String getOutTradeNo() {
                return outTradeNo;
            }

            public void setOutTradeNo(String outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public String getSellerId() {
                return sellerId;
            }

            public void setSellerId(String sellerId) {
                this.sellerId = sellerId;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getTradeNo() {
                return tradeNo;
            }

            public void setTradeNo(String tradeNo) {
                this.tradeNo = tradeNo;
            }

            public String getErrorCode() {
                return errorCode;
            }

            public void setErrorCode(String errorCode) {
                this.errorCode = errorCode;
            }

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }
        }
    }
}