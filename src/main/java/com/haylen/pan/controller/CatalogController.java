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
    public CommonResult create(@RequestParam(defaultValue = "0") Long parentId,
                               @RequestParam @NotEmpty String name) {
        Catalog catalog = catalogService.create(parentId, name);
        if (catalog == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(catalog);
    }

    @RequestMapping(value = "/list/{id}")
    public CommonResult list(@PathVariable Long id) {
        List<Catalog> list = catalogService.listChildCatalog(id);
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam(defaultValue = "0") Long newParentId,
                             @PathVariable Long id) {
        if (catalogService.move(newParentId, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success("");
    }

    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (catalogService.rename(newName, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success("");
    }
}
