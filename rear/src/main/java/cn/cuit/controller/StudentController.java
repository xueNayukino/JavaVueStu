package cn.cuit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.cuit.domain.Student;
import cn.cuit.service.StudentService;
import cn.cuit.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
//import java.util.UUID;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;

/**
 * 学生信息接口
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Value("${spring.upload.image-path}")
    private String uploadPath;
    
    @Value("${spring.upload.allowed-types}")
    private String allowedTypes;
    
    @Value("${spring.upload.image-url-prefix}")
    private String imageUrlPrefix;

    /**
     * 新增和修改学生信息
     */
    @PutMapping
    public AjaxResult addOrUpdateStudent(@RequestBody Student student) {
        if (studentService.saveOrUpdate(student)) {
            return AjaxResult.me();
        }
        return AjaxResult.me().setSuccess(false).setMessage("程序错误，请联系管理员！");
    }

    /**
     * 删除学生信息
     */
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Integer id) {
        try {
            if (studentService.removeById(id)) {
                return AjaxResult.me();
            }
            return AjaxResult.me().setSuccess(false).setMessage("删除失败！");
        } catch (Exception e) {
            return AjaxResult.me().setSuccess(false).setMessage("删除失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有学生信息
     */
    @GetMapping("/list")
    public List<Student> findAll() {
        return studentService.list();
    }

    /**
     * 根据ID查询学生信息
     */
    @GetMapping("/info/{id}")
    public Student findOne(@PathVariable Integer id) {
        return studentService.getById(id);
    }

    /**
     * 学生注册
     */
//    @PostMapping("/register")
//    public AjaxResult register(@RequestBody Student student) {
//        // 检查学号是否已存在
//        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("student_no", student.getStudentNo());
//        if (studentService.getOne(queryWrapper) != null) {
//            return AjaxResult.me()
//                    .setSuccess(false)
//                    .setMessage("该学号已被注册！");
//        }

//        // 加密密码
//        student.setPassword(PasswordEncoder.encode(student.getPassword()));
//
//        // 设置默认值
//        student.setRegistrationStatus(0);
//        student.setOnCampus(0);
//
//        if (studentService.save(student)) {
//            return AjaxResult.me().setMessage("注册成功");
//        }
//        return AjaxResult.me()
//                .setSuccess(false)
//                .setMessage("注册失败，请联系管理员");
//    }

    /**
     * 获取学生信息
     */
    @GetMapping("/home")
    public AjaxResult getStudentInfo(@RequestParam String id) {
        try {
            // 参数验证
            if (id == null || id.equals("undefined") || id.trim().isEmpty()) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("无效的学生ID");
            }

            // 转换ID
            Integer studentId;
            try {
                studentId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("学生ID格式错误");
            }

            // 查询学生信息
            Student student = studentService.getById(studentId);
            if (student == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("未找到学生信息");
            }

            // 构建数据
            Map<String, Object> studentData = new HashMap<>();
            if (student.getPhoto() != null && !student.getPhoto().isEmpty()) {
                studentData.put("photo", student.getPhoto());  // 直接使用文件名
            } else {
                studentData.put("photo", "default-avatar.jpg");  // 默认图片
            }
            studentData.put("name", student.getName());
            studentData.put("gender", student.getGender() != null ? student.getGender().toString() : "");
            studentData.put("age", student.getAge() != null ? student.getAge().toString() : "");
            
            // 处理入学年份格式
            if (student.getEnrollmentYear() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                String year = sdf.format(student.getEnrollmentYear());
                studentData.put("enrollmentYear", year);
            } else {
                studentData.put("enrollmentYear", "");
            }
            
            studentData.put("studentId", student.getStudentId().toString());
            studentData.put("major", student.getMajor() != null ? student.getMajor().toString() : "");
            studentData.put("password", student.getPassword());
            studentData.put("registrationStatus", student.getRegistrationStatus() != null ? 
                student.getRegistrationStatus().toString() : "未报到");
            studentData.put("phoneNumber", student.getPhoneNumber());
            studentData.put("idCard", student.getIdCard());

            Map<String, Object> result = new HashMap<>();
            result.put("student", studentData);

            return AjaxResult.me()
                .setSuccess(true)
                .setResult(result);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("获取学生信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生信息（用于信息修改页面）
     */
    @GetMapping("/change")
    public AjaxResult getStudentInfoForEdit(@RequestParam String id) {
        try {
            Integer studentId = Integer.parseInt(id);
            Student student = studentService.getById(studentId);
            
            if (student == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("未找到学生信息");
            }

            Map<String, Object> studentData = new HashMap<>();
            if (student.getPhoto() != null && !student.getPhoto().isEmpty()) {
                studentData.put("photo", student.getPhoto());  // 直接使用文件名
            } else {
                studentData.put("photo", "default-avatar.jpg");  // 默认图片
            }
            studentData.put("name", student.getName());
            studentData.put("gender", student.getGender() != null ? student.getGender().toString() : "");
            studentData.put("age", student.getAge() != null ? student.getAge().toString() : "");
            
            // 处理入学年份格式
            if (student.getEnrollmentYear() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                String year = sdf.format(student.getEnrollmentYear());
                studentData.put("enrollmentYear", year);
            } else {
                studentData.put("enrollmentYear", "");
            }
            
            studentData.put("studentId", student.getStudentId().toString());
            studentData.put("major", student.getMajor() != null ? student.getMajor().toString() : "");
            studentData.put("password", student.getPassword());
            studentData.put("registrationStatus", student.getRegistrationStatus() != null ? 
                student.getRegistrationStatus().toString() : "未报到");
            studentData.put("phoneNumber", student.getPhoneNumber());
            studentData.put("idCard", student.getIdCard());

            return AjaxResult.me()
                .setSuccess(true)
                .setResult(studentData);
            
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("获取学生信息失败: " + e.getMessage());
        }
    }

    /**
     * 修改学生信息
     */
    @PutMapping("/update/{studentId}")
    public AjaxResult updateStudent(@PathVariable Integer studentId, @RequestBody Student updateStudent) {
        try {
            // 获取现有学生信息
            Student student = studentService.getById(studentId);
            if (student == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("未找到学生信息");
            }

            // 更新学生信息
            if (updateStudent.getName() != null) {
                student.setName(updateStudent.getName());
            }
            if (updateStudent.getGender() != null) {
                student.setGender(updateStudent.getGender());
            }
            if (updateStudent.getAge() != null) {
                student.setAge(updateStudent.getAge());
            }
            if (updateStudent.getEnrollmentYear() != null) {
                // 处理入学年份
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                    java.util.Date date = sdf.parse(updateStudent.getEnrollmentYear().toString());
                    student.setEnrollmentYear(date);
                } catch (Exception e) {
                    // 如果日期解析失败，保持原值不变
                    System.out.println("入学年份解析失败: " + e.getMessage());
                }
            }
            if (updateStudent.getPhoneNumber() != null) {
                student.setPhoneNumber(updateStudent.getPhoneNumber());
            }
            if (updateStudent.getMajor() != null) {
                student.setMajor(updateStudent.getMajor());
            }
            if (updateStudent.getPassword() != null) {
                student.setPassword(updateStudent.getPassword());
            }
            if (updateStudent.getRegistrationStatus() != null) {
                student.setRegistrationStatus(updateStudent.getRegistrationStatus());
            }
            if (updateStudent.getIdCard() != null) {
                student.setIdCard(updateStudent.getIdCard());
            }
            if (updateStudent.getPhoto() != null) {
                student.setPhoto(updateStudent.getPhoto());
            }

            // 保存更新后的信息
            if (studentService.updateById(student)) {
                return AjaxResult.me()
                    .setSuccess(true)
                    .setMessage("更新成功");
            } else {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("更新失败: " + e.getMessage());
        }
    }

    /**
     * 处理照片上传
     */
    @PostMapping("/upload/photo")
    public AjaxResult uploadPhoto(@RequestParam("file") MultipartFile file, @RequestParam("studentId") String studentId) {
        try {
            // 获取项目根目录
            String projectRoot = new File("").getAbsolutePath();
            File uploadDir = new File(projectRoot, uploadPath);  // 使用注入的uploadPath
            System.out.println("Upload directory: " + uploadDir.getAbsolutePath());

            // 确保目录存在
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    System.err.println("Failed to create directory: " + uploadDir.getAbsolutePath());
                    return AjaxResult.me()
                        .setSuccess(false)
                        .setMessage("创建上传目录失败");
                }
            }

            // 检查文件类型和大小
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("只能上传图片文件");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("图片大小不能超过5MB");
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String fileName = "student_" + studentId + "_" + System.currentTimeMillis() + extension;

            // 保存文件
            File destFile = new File(uploadDir, fileName);
            try {
                Files.copy(file.getInputStream(), destFile.toPath(), 
                          java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("保存文件失败: " + e.getMessage());
            }

            // 更新学生信息
            Student student = studentService.getById(Integer.parseInt(studentId));
            if (student != null) {
                student.setPhoto(fileName);
                studentService.updateById(student);
            }

            return AjaxResult.me()
                .setSuccess(true)
                .setMessage("上传成功")
                .setResult(new HashMap<String, String>() {{
                    put("url", fileName);
                }});

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取图片
     */
    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            // 获取项目根目录
            String projectRoot = new File("").getAbsolutePath();
            File file = new File(projectRoot, uploadPath + "/" + fileName);
            
            if (!file.exists() || !file.canRead()) {
                // 如果文件不存在或无法读取，返回默认图片
                file = new File(projectRoot, uploadPath + "/default-avatar.jpg");
                if (!file.exists() || !file.canRead()) {
                    return ResponseEntity.notFound().build();
                }
            }

            Resource resource = new FileSystemResource(file);
            MediaType mediaType = MediaType.IMAGE_JPEG;
            if (fileName.toLowerCase().endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.toLowerCase().endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }

            return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .header("Access-Control-Allow-Origin", "*")  // 添加CORS头
                .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 学生签到
     */
    @PostMapping("/student-registe")
    @ResponseBody
    public AjaxResult studentRegister(@RequestBody Map<String, String> request) {
        try {
            String idCard = request.get("idCard");
            System.out.println("收到签到请求，身份证号：" + idCard);

            if (idCard == null || idCard.trim().isEmpty()) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("身份证号不能为空");
            }

            // 根据身份证号查询学生
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_card", idCard);
            Student student = studentService.getOne(queryWrapper);
            
            System.out.println("查询结果：" + student);

            if (student == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("未找到学生信息")
                    .setResult(new HashMap<String, Object>() {{
                        put("exists", false);
                    }});
            }

            // 检查当前状态
            String currentStatus = student.getRegistrationStatus();
            System.out.println("当前签到状态：" + currentStatus);

            // 检查是否已经签到
            if ("已报道".equals(currentStatus)) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("该学生已完成签到")
                    .setResult(new HashMap<String, Object>() {{
                        put("exists", true);
                    }});
            }

            // 更新签到状态
            student.setRegistrationStatus("已报道");
            student.setLastModifyDate(new Date());
            studentService.updateById(student);

            return AjaxResult.me()
                .setSuccess(true)
                .setMessage("签到成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("签到失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生签到状态
     */
    @GetMapping("/registration-status/{studentId}")
    public AjaxResult getRegistrationStatus(@PathVariable Integer studentId) {
        try {
            Student student = studentService.getById(studentId);
            if (student == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("未找到学生信息");
            }

            return AjaxResult.me()
                .setSuccess(true)
                .setResult(new HashMap<String, Object>() {{
                    put("status", student.getRegistrationStatus());
                    put("lastModifyDate", student.getLastModifyDate());
                }});

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("获取签到状态失败: " + e.getMessage());
        }
    }
} 