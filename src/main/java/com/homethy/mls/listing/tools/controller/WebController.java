package com.homethy.mls.listing.tools.controller;

import com.homethy.mls.listing.tools.model.QueryMessage;
import com.homethy.mls.listing.tools.model.User;
import com.homethy.offlineservice.source.mls.rets.RetsSearchTool;
import org.realtors.rets.client.*;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jia on 17-7-6.
 * 路由控制器
 * 前三个用来返回页面
 * 后两个用来前后端的参数传递
 * "http://rets2.navicamls.net/login.aspx", "Rets413-100716","Rets413AzG"
 */
@Controller
public class WebController {

    private final static String fileName = "history.txt";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 关于session的保存问题，ThreadLocal加以改进，或者增加一个拦截器
    private static RetsSession retsSession;
    private static MResource[] resources;
    private static MClass[] mClasses;
    private final static SimpleDateFormat str =
            new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
    private String ip = null;

    @RequestMapping("/")
    public String index() {
        return "forward:/index";
    }

    @RequestMapping("/index")
    /**
     * 登录界面，User用来获取登录信息
     */
    public String index(@ModelAttribute User user, Model model) {
        logger.info("=============首页首页===========");
        if (user.getLogin_url() != null)
            model.addAttribute("user", user);
        else
            model.addAttribute("user", new User());
        logger.info("用户信息：" + user.toString());
        return "index";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    /**
     * 登录处理逻辑，前端获取User的信息，然后验证;
     * 然后回传一个QueryMessage，用来获取查询参数
     * @param user 登录信息
     * @param model 回传参数
     */
    public String login(@ModelAttribute User user, Model model) {
        logger.info("用户登录信息：" + user.toString());
        model.addAttribute("queryMessage", new QueryMessage());
        if (retsSession != null && retsSession.getLoginUrl() != null)
            return "login";
        // rets_version 版本构造，默认1.7.2
        String retsVer = "".equals(user.getRets_version()) ? "1.7.2": user.getRets_version();
        RetsVersion retsVersion = RetsVersion.getVersion(retsVer);
        try {
            // 没有user_agent信息时，登录方法
            if ("".equals(user.getUser_agent()))
                retsSession = getRetsSessionByDefault(user.getLogin_url(), user.getUsername(),
                        user.getPassword(), retsVersion);
            else {
                String ua_pwd = "".equals(user.getUa_pwd()) ? null: user.getUa_pwd();

                retsSession = getRetsSessionByUserAgent(user.getLogin_url(), user.getUsername(),
                        user.getPassword(), user.getUser_agent(), ua_pwd, retsVersion);
            }
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (RetsException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
//            try {
//                retsSession.logout();
//                retsSession = null;
//            } catch (RetsException e1) {
//                logger.error(e1.getMessage());
//                e1.printStackTrace();
//            } finally {
            return "incorrect";
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "incorrect";
        }
        String loginUrl = retsSession.getLoginUrl();
        model.addAttribute("ip", ip);
        model.addAttribute("loginUrl", loginUrl);
        logger.info("ip:" + ip);
        model.addAttribute("dataMessage", str.format(new Date()));
        logger.info("登录成功");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        logger.info("用户登出ing");
        if (retsSession != null) {
            try {
                retsSession.logout();
            } catch (RetsException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            retsSession = null;
            resources = null;
            mClasses = null;
        }
        logger.info("用户成功登出");

        return "redirect:/index";
    }

    @RequestMapping("/incorrect")
    /**
     * 用户无法验证时返回的错误页面
     */
    public String incorrect(Model model) {
        logger.info("用户无法验证， 登录未成功");
        return "incorrect";
    }


    @RequestMapping(value = "/search",method = RequestMethod.POST)
    /**
     * 查询方法，这里使用ajax前后交互
     */
    public @ResponseBody Object
    search(@RequestBody QueryMessage query) throws Exception{
        // 写文件
        logger.info("查询条件：" + query.toString());
        String context = query.toString();
        try {
            writeFile(context);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
//        System.out.println(query);
        // 调用查询接口
        List<Map<String, String>> searchResultList =
                RetsSearchTool.searchDataFromMLS(retsSession, query.getResource(),
                        query.getSclass(), query.getQuery(),
                        "", 100, 1);
        logger.info("查询结果：共" + searchResultList.size() + "条记录");
        return searchResultList;
    }


    @RequestMapping("/history")
    /**
     * 关于查询记录的显示问题，由于filename是全局的，
     * 所以会显示所有人的查询记录，改进方法暂时未想到。
     * 这里也是通过ajax方法前后交互。
     */
    public @ResponseBody String history() {
        // 读取文件
        String s = null;
        try {
            s = readFile();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("历史记录：" + s);
        return s;
    }

    /**
     * 页面加载即可回传参数
     * @return object
     */
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public @ResponseBody
    Object resources() {
        logger.info("验证登录信息。请求resource数组");
        if (retsSession == null) {
            logger.info("用户未登录，请登录");
            return null;
        }
        try {
            if (resources == null)
                resources = retsSession.getMetadata().getSystem().getMResources();
        } catch (RetsException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("返回resource的数组");
        return resources;
    }

    /**
     * 在改变resources后，回传级联的class
     * @param index
     * @return
     */
    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public @ResponseBody
    Object getSclass(@RequestParam int index) {
        logger.info("验证登录信息。请求class数组");
        if (retsSession == null) {
            logger.info("用户未登录，请登录");
            return null;
        }
        if (index == -1)
            return "";
        mClasses = resources[index].getMClasses();
        logger.info("返回class的数组");
        return mClasses;
    }

    /**
     * 获取数据列表信息
     * @param index
     * @return
     */
    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public @ResponseBody
    Object getTableName(@RequestParam int index) {
        logger.info("验证登录信息，请求table数组");
        if (retsSession == null) {
            logger.info("用户未登录，请登录");
            return null;
        }
        if (index == -1)
            return null;
        MTable[] tables = mClasses[index].getMTables();
        logger.info("返回table数组，size为：" + tables.length);
        return tables;
    }
    /**
     * 将记录写入文件
     * @param content 写入的内容
     * @throws IOException 文件读取写入失败抛出异常
     */
    private static void
    writeFile(String content) throws IOException{

        File file = new File(fileName);

        if (!file.exists())
            file.createNewFile();
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(file.getName(), true));
        writer.write(content);
        writer.write("\n");
        writer.flush();
        writer.close();
    }

    /**
     * 读取文件,从最后一行开始读取
     * @return s 文件的全部字符
     * @throws IOException 文件读取失败抛出异常
     */
    private static String
    readFile() throws IOException {
        RandomAccessFile reader = new RandomAccessFile(fileName, "r");
        StringBuilder sb = new StringBuilder();
        long len = reader.length();
        long start = reader.getFilePointer();
        long next = start + len - 1;
        String line;
        reader.seek(next);
        int c = -1;
        while (next > start) {
            c = reader.read();
            if (c == '\n' || c == '\r') {
                line = reader.readLine();
                if (line != null)
                    sb.append(line + "<br/>");
                next--;
            }
            next--;
            reader.seek(next);
            if (next == 0) {// 当文件指针退至文件开始处，输出第一行
                 sb.append(reader.readLine() + "<br/>");
//                System.out.println(reader.readLine());
            }
        }
        reader.close();
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 使用默认方式认证
     *
     * @param loginUrl
     * @param userName
     * @param password
     * @param retsVersion
     * @return session
     */
    private static RetsSession
    getRetsSessionByDefault(
            String loginUrl, String userName,
            String password, RetsVersion retsVersion)
            throws RetsException {

        RetsHttpClient httpClient = new CommonsHttpClient();
        RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);
        session.setMethod("POST");
        session.login(userName, password);
        return session;
    }

    /**
     * 使用useragent的信息认证，获取session
     *
     * @param loginUrl
     * @param userName
     * @param password
     * @param retsVersion
     * @return session
     */
    private static RetsSession
    getRetsSessionByUserAgent (
            String loginUrl, String userName, String password,
            String userAgent, String userAgentPassword, RetsVersion retsVersion)
            throws RetsException {

        RetsHttpClient httpClient = new CommonsHttpClient(
                10000, userAgentPassword, true);

        RetsSession session = new RetsSession(
                loginUrl, httpClient, retsVersion, userAgent, false);
        session.setMethod("POST");
        session.login(userName, password);

        return session;
    }

}
