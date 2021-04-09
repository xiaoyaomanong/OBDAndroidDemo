package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2020/6/4 0004 10:42
 */
public class AlipayResultEntity {


    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2021001163661262","auth_app_id":"2021001163661262","charset":"utf-8","timestamp":"2020-06-04 11:04:41","out_trade_no":"1683","total_amount":"0.01","trade_no":"2020060422001443331410813000","seller_id":"2088831520306734"}
     * sign : Vfh+BKi28Q27jLLvmE7mkkdJv+Nlrm5f4t/xYSsrG0GpAcVdQaRlPZUDDxwTMULveEX8q1H1NpLIhGweiydIFEQptqZVO1HKV7RXizaALDc2qrie71+wP1tTuPdJJG1UGfFbi4vSte+Q5d/FWTwGa16FDg3Yn7ZVylaGlfTJfkg8T/1wV2f7sp96/KaG2Iuo82BaiUorX2ndhLo/MNoYL+AH7uwA5cgKCfsO9Rj8AJt44Jm93u/vyKzuBSojol/M558mCh6MSIVWACjJr8mbZQJwn5SuQAEW9W4THT/9B2lc9siO/neQfMmWpeOJe6YdRukK0lUfU1wEeKQUubVJUg==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseEntity alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseEntity getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseEntity alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseEntity {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2021001163661262
         * auth_app_id : 2021001163661262
         * charset : utf-8
         * timestamp : 2020-06-04 11:04:41
         * out_trade_no : 1683
         * total_amount : 0.01
         * trade_no : 2020060422001443331410813000
         * seller_id : 2088831520306734
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String out_trade_no;
        private String total_amount;
        private String trade_no;
        private String seller_id;

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

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }
    }
}
