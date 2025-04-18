package cn.cuit.controller;

import cn.cuit.domain.Student;
import cn.cuit.domain.Log;
import cn.cuit.service.LogService;
import cn.cuit.service.StudentService;
import cn.cuit.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminManagementController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private LogService logService;

    @GetMapping("/getStudents")
    public ResponseEntity<ResponseWrapper<List<Student>>> getStudents() {
        log.info("获取学生列表请求");
        try {
            List<Student> students = studentService.list();
            return ResponseEntity.ok(new ResponseWrapper<>(true, students));
        } catch (Exception e) {
            log.error("获取学生列表失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "获取学生列表失败"));
        }
    }

    @GetMapping("/searchStudent")
    public ResponseEntity<ResponseWrapper<List<Student>>> searchStudent(@RequestParam String keyword) {
        log.info("搜索学生请求，关键字: {}", keyword);
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "搜索关键字不能为空"));
            }

            LambdaQueryWrapper<Student> queryWrapper = Wrappers.<Student>lambdaQuery()
                    .like(Student::getStudentId, keyword.trim())
                    .or()
                    .like(Student::getStudentName, keyword.trim())
                    .or()
                    .like(Student::getName, keyword.trim());

            List<Student> students = studentService.list(queryWrapper);
            return ResponseEntity.ok(new ResponseWrapper<>(true, students));
        } catch (Exception e) {
            log.error("搜索学生失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "搜索失败"));
        }
    }

    @PostMapping("/updateStudent")
    public ResponseEntity<ResponseWrapper<Student>> updateStudent(@RequestBody Student student) {
        log.info("更新学生信息请求，接收到的数据: {}", student);
        try {
            if (student == null || student.getStudentId() == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "学生ID不能为空"));
            }

            // 获取原有学生信息
            Student existingStudent = studentService.getById(student.getStudentId());
            if (existingStudent == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "学生不存在"));
            }

            // 保留原有的敏感字段
            student.setPassword(existingStudent.getPassword());
            student.setPhoto(existingStudent.getPhoto());
            student.setIdCard(existingStudent.getIdCard());
            
            // 设置更新时间
            student.setLastModifyDate(new Date());

            // 更新学生信息
            boolean success = studentService.updateById(student);
            
            if (success) {
                try {
                    // 记录操作日志
                    Log operationLog = new Log();
                    operationLog.setModifyOperationType(Log.OPERATION_MODIFY);
                    operationLog.setModifyContent("更新学生信息: ID=" + student.getStudentId() + 
                                               ", 姓名=" + student.getName());
                    operationLog.setModifyTime(new Date());
                    operationLog.setStudentId(student.getStudentId());
                    logService.save(operationLog);
                } catch (Exception logError) {
                    log.warn("记录日志失败，但不影响主要业务: ", logError);
                }

                // 获取更新后的学生信息并返回
                Student updatedStudent = studentService.getById(student.getStudentId());
                return ResponseEntity.ok(new ResponseWrapper<>(true, updatedStudent, "更新成功"));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "更新失败"));
            }
        } catch (Exception e) {
            log.error("更新学生信息失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteStudent(@PathVariable Integer studentId) {
        log.info("删除学生请求: {}", studentId);
        try {
            if (studentId == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "学生ID不能为空"));
            }

            // 获取学生信息用于日志记录
            Student student = studentService.getById(studentId);
            if (student == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "学生不存在"));
            }

            boolean success = studentService.removeById(studentId);
            if (success) {
                // 记录删除操作日志
                Log operationLog = new Log();
                operationLog.setModifyOperationType(Log.OPERATION_MODIFY);
                operationLog.setModifyContent("删除学生: ID=" + studentId + 
                                           ", 姓名=" + student.getName());
                operationLog.setModifyTime(new Date());
                operationLog.setStudentId(studentId);
                try {
                    logService.save(operationLog);
                } catch (Exception logError) {
                    log.warn("记录日志失败，但不影响主要业务: ", logError);
                }

                return ResponseEntity.ok(new ResponseWrapper<>(true, null, "删除成功"));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "删除失败"));
            }
        } catch (Exception e) {
            log.error("删除学生失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "删除失败: " + e.getMessage()));
        }
    }
}
