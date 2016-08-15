package com.hand.demo.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * DTO基类.
 * 
 * @author guanghui.liu
 */
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 8094250686230173621L;

    /**
     * 防篡改校验字段(非数据库字段).
     */
    @JsonInclude(Include.NON_NULL)
    private String _token;

    /**
     * Record的版本号，update时自增.
     */
    @JsonIgnore
    private Long version;

    @JsonIgnore
    private Long createdBy;

    @JsonIgnore
    private Date creationDate;

    @JsonIgnore
    private Long lastUpdatedBy;

    @JsonIgnore
    private Date lastUpdateDate;

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
