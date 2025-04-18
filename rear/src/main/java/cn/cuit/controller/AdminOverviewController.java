package cn.cuit.controller;

import cn.cuit.domain.Student;
import cn.cuit.service.StudentService;
import cn.cuit.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminOverviewController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getTotalData")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getTotalData() {
        log.info("获取整体学生数据请求");
        try {
            List<Student> students = studentService.list();

            // 统计数据
            int totalStudent = students.size();
            int boyCount = (int) students.stream()
                    .filter(s -> "男".equals(s.getGender()))
                    .count();
            int girlCount = totalStudent - boyCount;
            
            // 修改为"已报道"而不是"已报到"
            int reportedStudent = (int) students.stream()
                    .filter(s -> "已报道".equals(s.getRegistrationStatus()))
                    .count();

            // 按专业统计
            List<Map<String, Object>> majorList = students.stream()
                    .collect(Collectors.groupingBy(
                            Student::getMajor,
                            Collectors.counting()
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("major", entry.getKey());
                        map.put("count", entry.getValue());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 按入学年份统计
            List<Map<String, Object>> enrollmentYearData = students.stream()
                    .collect(Collectors.groupingBy(
                            student -> {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(student.getEnrollmentYear());
                                return String.valueOf(cal.get(Calendar.YEAR));
                            },
                            Collectors.counting()
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("year", entry.getKey());
                        map.put("count", entry.getValue());
                        return map;
                    })
                    .sorted(Comparator.comparing(m -> m.get("year").toString()))
                    .collect(Collectors.toList());

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("totalStudent", totalStudent);
            result.put("boyCount", boyCount);
            result.put("girlCount", girlCount);
            result.put("reportedStudent", reportedStudent);
            result.put("majorList", majorList);
            result.put("enrollmentYearData", enrollmentYearData);

            return ResponseEntity.ok(new ResponseWrapper<>(true, result));
        } catch (Exception e) {
            log.error("获取统计数据失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "获取统计数据失败"));
        }
    }
}
