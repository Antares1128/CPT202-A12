package com.cy.store.util;
import java.io.Serializable;
/**
 * Response result class
 * @param <E> Type of response data
 */
public class JsonResult<E> implements Serializable {
    /** status code */
    private Integer state;
    /** state description information */
    private String message;
    /** data */
    private E data;

    public JsonResult() {
        super();
    }
    public JsonResult(Integer state) {
        super();
        this.state = state;
    }
    /** Called when an exception occurs */
    public JsonResult(Throwable e) {
        super();
        // Get exception information in exception object
        this.message = e.getMessage();
    }
    public JsonResult(Integer state, E data) {
        super();
        this.state = state;
        this.data = data;
    }
    // Generate: Getter and Sette

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
