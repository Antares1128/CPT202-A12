package com.cy.store.controller;
import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/** The base class of the controller class */
public class BaseController {
    /** Status code of the successful operation */
    public static final int OK = 200;
    /** @ExceptionHandler Used to handle exceptions thrown by methods uniformly */

    @ExceptionHandler({ServiceException.class,FileUploadException.class})
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicateException) {
            result.setState(4000);
            result.setMessage("The user name has been occupied");
        } else if (e instanceof UserNotFoundException) {
            result.setState(5001);
            result.setMessage("User data does not exist");
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
            result.setMessage("The user name and password are incorrect");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("An unknown exception occurred during registration");
        }else if (e instanceof UpdateException) {
            result.setState(5001);
            result.setMessage("Unknown exception occurred while updating data");
        }else if (e instanceof FileEmptyException) {
            result.setState(6000);
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
        } else if (e instanceof FileStateException) {
            result.setState(6003);
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
        }else if (e instanceof AddressCountLimitException) {
            result.setState(4003);
            result.setMessage("The user's shipping address exceeded the upper limit");
        }else if (e instanceof AddressNotFoundException) {
            result.setState(4004);
            result.setMessage("The user's shipping address data does not exist");
        } else if (e instanceof AccessDeniedException) {
            result.setState(4005);
            result.setMessage("The receiving address data is improperly accessed");
        }


        return result;
    }

    protected final Integer getUidFromSession(HttpSession session){
        return Integer.valueOf(session.getAttribute("uid").toString());

    }

    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }

}