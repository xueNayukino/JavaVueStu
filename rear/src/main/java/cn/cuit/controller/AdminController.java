package cn.cuit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.cuit.utils.AjaxResult;
import cn.cuit.domain.Admin;
import cn.cuit.domain.Student;
import cn.cuit.domain.LoginRequest;
import cn.cuit.service.AdminService;
import cn.cuit.service.StudentService;
import cn.cuit.utils.PasswordEncoder;
import cn.cuit.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 管理员接口
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    /**
     * 统一登录接口
     */
    @PostMapping("/admin/login")
    public AjaxResult login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("接收到登录请求: " + loginRequest);
            
            if ("admin".equals(loginRequest.getType())) {
                return adminLogin(loginRequest);
            } else if ("student".equals(loginRequest.getType())) {
                return studentLogin(loginRequest);
            } else {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("无效的用户类型");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("服务器错误：" + e.getMessage());
        }
    }

    /**
     * 管理员登录处理
     */
    private AjaxResult adminLogin(LoginRequest loginRequest) {
        try {
            System.out.println("管理员登录请求 - 账号: " + loginRequest.getAccount());
            
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("admin_id", "username", "password")
                       .eq("username", loginRequest.getAccount());
                       // 先不加密密码，直接查询用户
            
            Admin admin = adminService.getOne(queryWrapper);
            
            if (admin != null) {
                // 如果是明文密码，直接比较
                if (loginRequest.getPassword().equals(admin.getPassword())) {
                    String token = JwtUtil.generateToken(String.valueOf(admin.getAdminId()), "admin");
                    admin.setPassword(null);  // 清除密码后再返回
                    
                    // 构建返回的管理员信息
                    Map<String, Object> adminInfo = new HashMap<>();
                    adminInfo.put("adminId", admin.getAdminId());
                    
                    return AjaxResult.me()
                            .setSuccess(true)
                            .setMessage("管理员登录成功")
                            .setResult(adminInfo)
                            .setToken(token);
                }
            }
            return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("账户或密码错误！");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("登录失败：" + e.getMessage());
        }
    }

    /**
     * 学生登录处理
     */
    private AjaxResult studentLogin(LoginRequest loginRequest) {
        try {
            System.out.println("学生登录请求 - 账号: " + loginRequest.getAccount());
            
            // 转换ID
            Integer studentId;
            try {
                studentId = Integer.parseInt(loginRequest.getAccount());
            } catch (NumberFormatException e) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("账号格式错误");
            }
            
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("student_id", studentId);
            
            Student student = studentService.getOne(queryWrapper);
            
            if (student != null) {
                if (loginRequest.getPassword().equals(student.getPassword())) {
                    String token = JwtUtil.generateToken(String.valueOf(student.getStudentId()), "student");
                    
                    // 构建返回的学生信息
                    Map<String, Object> studentInfo = new HashMap<>();
                    studentInfo.put("studentId", student.getStudentId());  // 直接返回数字类型的ID
                    student.setPassword(null);  // 清除密码后再返回
                    
                    System.out.println("登录成功，返回数据: " + studentInfo);  // 添加日志
                    
                    return AjaxResult.me()
                        .setSuccess(true)
                        .setMessage("学生登录成功")
                        .setResult(studentInfo)
                        .setToken(token);
                }
            }
            
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("账户或密码错误！");
            
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("登录失败：" + e.getMessage());
        }
    }

    /**
     * 管理员注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody Admin admin) {
        // 检查用户名是否已存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", admin.getUsername());
        if (adminService.getOne(queryWrapper) != null) {
            return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("该用户名已被注册！");
        }

        // 加密密码
        admin.setPassword(PasswordEncoder.encode(admin.getPassword()));

        if (adminService.save(admin)) {
            return AjaxResult.me().setMessage("注册成功");
        }
        return AjaxResult.me()
                .setSuccess(false)
                .setMessage("注册失败，请联系系统管理员");
    }

    @GetMapping
    public List<Admin> getAdmins(){
    List <Admin> list=adminService.list();
    return  list;
    }


    @GetMapping("/{id}")//http://localhost:8080/admin/x
    public Admin getAdmin(@PathVariable("id") int id) {
        Admin byId = adminService.getById(id);
        return byId;
    }

    /**
     * 新增或者修改
     * 根据id决定
     * @param admin
     * @return
     */
    @PutMapping//http://localhost:8080/admin
    public AjaxResult addOrUpdateAdmin(@RequestBody Admin admin) {//@RequestBody 将前端的json对象转化为实体对象
        if (adminService.saveOrUpdate(admin)){
            return AjaxResult.me();
        }
        return AjaxResult.me().setSuccess(false).setMessage("程序错误，请联系管理员！");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public AjaxResult deleteAdmin(@PathVariable("id") int id) {
        if (adminService.removeById(id)){
            return AjaxResult.me();
        }
        return AjaxResult.me().setSuccess(false).setMessage("删除失败，请联系管理员！");
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }

    @GetMapping("/test-db")
    public String testDB() {
        try {
            // 尝试查询一条记录
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.last("LIMIT 1");
            Admin admin = adminService.getOne(queryWrapper);
            return "数据库连接正常" + (admin != null ? ", 查询到记录" : ", 无记录");
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库连接失败: " + e.getMessage();
        }
    }
}

