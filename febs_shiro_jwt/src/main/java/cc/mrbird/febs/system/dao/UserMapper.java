package cc.mrbird.febs.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cc.mrbird.febs.system.domain.User;

public interface UserMapper extends BaseMapper<User> {

    IPage<User> findUserDetail(Page page, @Param("user") User user);
    IPage<User> findByDeptId(Page page, @Param("deptId") String deptId);
    IPage<User> findUserByDept(Page page, @Param("deptId") String deptId);
    /**
     * 获取单个用户详情
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findDetail(String loginName);
    User findOneUser(String loginName);
    List<User> findUserByName(String Name);
}