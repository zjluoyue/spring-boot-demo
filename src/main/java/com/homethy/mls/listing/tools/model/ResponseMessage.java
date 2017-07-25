package com.homethy.mls.listing.tools.model;

import java.util.List;
import java.util.Map;

public class ResponseMessage {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private Meta meta;
    private Object data;


    public ResponseMessage success() {
        this.meta = new Meta(true, OK);
        return this;
    }

    public ResponseMessage success(Object data) {
        this.meta = new Meta(true, OK);
        this.data = data;
        return this;
    }

    public ResponseMessage success(String message, Object data) {
        this.meta = new Meta(true, message);
        this.data = data;
        return this;
    }

    public ResponseMessage failure() {
        this.meta = new Meta(false, ERROR);
        return this;
    }

    public ResponseMessage failure(String message) {
        this.meta = new Meta(false, message);
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }

    public class Meta {

        private boolean success;
        private String message;

        public Meta(boolean success) {
            this.success = success;
        }

        public Meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
