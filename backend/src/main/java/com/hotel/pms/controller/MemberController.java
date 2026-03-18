package com.hotel.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.pms.entity.Member;
import com.hotel.pms.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@Tag(name = "会员管理", description = "会员相关接口")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/list")
    @Operation(summary = "获取所有会员", description = "获取酒店所有会员信息")
    public List<Member> list() {
        return memberService.list();
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取会员", description = "分页获取酒店会员信息")
    public IPage<Member> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam int current,
            @Parameter(description = "每页大小", example = "10") @RequestParam int size) {
        return memberService.page(new Page<>(current, size));
    }

    @PostMapping("/save")
    @Operation(summary = "保存会员", description = "新增或保存会员信息")
    public boolean save(@Parameter(description = "会员信息") @RequestBody Member member) {
        return memberService.save(member);
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员", description = "更新会员信息")
    public boolean update(@Parameter(description = "会员信息") @RequestBody Member member) {
        return memberService.updateById(member);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除会员", description = "根据ID删除会员")
    public boolean delete(@Parameter(description = "会员ID", example = "1") @PathVariable Long id) {
        return memberService.removeById(id);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取会员", description = "根据ID获取会员信息")
    public Member get(@Parameter(description = "会员ID", example = "1") @PathVariable Long id) {
        return memberService.getById(id);
    }
}