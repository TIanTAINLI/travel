package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //接收rname线路名称
        String rname = request.getParameter("rname");
        if (!"null".equals(rname) && !"undefined".equals(rname) && !"".equals(rname)) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        } else {
            rname = "%";
        }
        //2. 处理参数
        int cid = 0;//类别id
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码, 如果不传递, 则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示条数, 如果不传递, 默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }
        //3. 调用Service查询pageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
        //4. 将pageBean对象序列化为JSON
        writeValue(pb, response);
    }

    /**
     * 根据ID查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 接收ID
        String rid = request.getParameter("rid");
        //2. 调用Service查询Route对象
        Route route = routeService.findOne(rid);
        //3. 转为JSON写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     *
     * @param request
     * @param response
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 获取线路ID
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户尚未登录
            uid = 0;
        } else {
            //用户已经登录
            uid = user.getUid();
        }
        //3. 调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);
        //4. 写回客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) {
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户尚未登录
            return;
        } else {
            //用户已经登录
            uid = user.getUid();
        }
        //3. 调用Service添加
        favoriteService.add(rid, uid);
    }
}
