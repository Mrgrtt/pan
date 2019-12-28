package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.entity.Catalog;
import com.haylen.pan.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author  haylen
 * @date 2019-12-27
 */
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestParam(name = "parent_id", defaultValue = "0") Long parentId,
                               @NotEmpty @RequestParam String name) {
        if (catalogService.create(parentId, name) == null) {
            return CommonResult.failed("创建目录失败");
        }
        return CommonResult.success("", "创建目录成功");
    }

    @RequestMapping(value = "/list/{id}")
    public CommonResult list(@PathVariable Long id) {
        List<Catalog> list = catalogService.listChildCatalog(id);
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public CommonResult move(@RequestParam(name = "new_parent_id", defaultValue = "0") Long newParentId,
                             @RequestParam(required = true) Long id) {
        if (catalogService.move(newParentId, id) <= 0) {
            return CommonResult.failed("移动目录失败");
        }
        return CommonResult.success("", "移动目录成功");
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public CommonResult rename(@NotEmpty @RequestParam(name = "new_name") String newName,
                               @RequestParam(required = true) Long id) {
        if (catalogService.rename(newName, id) == 0) {
            return CommonResult.failed("重命名失败");
        }
        return CommonResult.success("", "重命名目录成功");
    }
}
