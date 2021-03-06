package com.cy.store.controller;
import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicateException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.xml.transform.OutputKeys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.cy.store.controller.BaseController.OK;

/** A controller class that handles user-related requests */
@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @RequestMapping("reg")
    //@ResponseBody//The response result of this method is sent to the front end in JSON format
    public JsonResult<Void> reg(User user) {

        userService.reg(user);

        return new JsonResult<>(OK);
    }

    @RequestMapping("login")
    public JsonResult<User>login(String username, String password, HttpSession session){
        User data = userService.login(username, password);

        // Complete binding of data to session objects (session global)
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());

        //Gets the data bound in the session
        System.out.println(getUidFromSession(session));
        System.out.println(getUsernameFromSession(session));

        // Wrap the above return value and status code OK into the response result and return
        return new JsonResult<User>(OK, data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword, String newPassword,
                                           HttpSession session) {
        // Call session.getAttribute("") to get the UID and username
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // Call the business object to change the password
        userService.changePassword(uid, username, oldPassword, newPassword);
        // Return to success
        return new JsonResult<Void>(OK);


    }

    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session) {
        User data = userService.getByUid(getUidFromSession(session));
        return new JsonResult<>(OK,data);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {
        // Get the UID and USERNAME from the HttpSession object
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // Call the business object to modify the user profile
        userService.changeInfo(uid, username, user);
        // In response to success
        return new JsonResult<Void>(OK);
    }
    //Set the maximum number of uploaded files
    public static final int AVATAR_MAX_SIZE = 10 * 1024 *1024;

    //Sets the type of the file
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }

    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session, @RequestParam("file") MultipartFile file){

        if(file.isEmpty()){
            throw new FileEmptyException("The file is empty");
        }
        if(file.getSize() > AVATAR_MAX_SIZE){
            throw new FileSizeException("File out of limit");
        }
        String contentType = file.getContentType();
        if(!AVATAR_TYPE.contains(contentType)){
            throw new FileTypeException("File type not supported");
        }
        String parent = session.getServletContext().getRealPath("upload");
        File dir = new File(parent);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        System.out.println("OriginalFilename =" + originalFilename);

        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);

        String filename = UUID.randomUUID().toString().toUpperCase() + suffix;
        File dest = new File(dir, filename);//Empty file
        //Write data from file to dest file
        try {
            file.transferTo(dest);//Write data from file to dest file
        } catch (FileStateException e) {
            throw new FileStateException("Abnormal File status");
        }catch (IOException e) {
            throw new FileUploadIOException("File read/write exception");
        }

        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        //Returns the path to the avatar
        String avatar = "/upload/" +filename;
        userService.changeAvatar(uid, avatar, username);
        //Return the path of the user's avatar to the front end for future avatar display
        return new JsonResult<>(OK, avatar);

    }
    /*
    @RequestMapping("reg")
    //@ResponseBody//?????????????????????????????????json???????????????????????????????????????
    public JsonResult<Void> reg(User user) {
    // ???????????????
    JsonResult<Void> result = new JsonResult<Void>();

    try {
        // ??????????????????????????????
        userService.reg(user);
        // ????????????
        result.setState(200);
        result.setMessage("??????????????????");
    } catch (UsernameDuplicateException e) {
    // ??????????????????
        result.setState(4000);
        result.setMessage("????????????????????????");
    } catch (InsertException e) {
    // ??????????????????
        result.setState(5000);
        result.setMessage("???????????????????????????????????????");
    }
    return result;
    }
    */
}
