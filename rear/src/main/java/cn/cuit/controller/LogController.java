package cn.cuit.controller;

//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.cuit.domain.Log;
import cn.cuit.service.LogService;
import cn.cuit.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志信息接口
 */
@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 新增和修改日志信息
     */
    @PutMapping
    public AjaxResult addOrUpdateLog(@RequestBody Log log) {
        if (logService.saveOrUpdate(log)) {
            return AjaxResult.me();
        }
        return AjaxResult.me().setSuccess(false).setMessage("程序错误，请联系管理员！");
    }

    /**
     * 删除日志信息
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return logService.removeById(id);
    }

    /**
     * 批量删除日志信息
//     */
//    @PostMapping("/del/batch")
//    public boolean deleteBatch(@RequestBody List<Integer> ids) {
//        return logService.removeByIds(ids);
//    }

    /**
     * 查询所有日志信息
     */
    @GetMapping
    public List<Log> findAll() {
        return logService.list();
    }

    /**
     * 根据ID查询日志信息
     */
    @GetMapping("/{id}")
    public Log findOne(@PathVariable Integer id) {
        return logService.getById(id);
    }

    /**
     * 分页查询日志信息
     */
//    @GetMapping("/page")
//    public Page<Log> findPage(@RequestParam Integer pageNum,
//                             @RequestParam Integer pageSize,
//                             @RequestParam(defaultValue = "") String search) {
//        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
//        if (!"".equals(search)) {
//            queryWrapper.like("operation", search)
//                    .or().like("operator", search);
//        }
//        return logService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }
} 