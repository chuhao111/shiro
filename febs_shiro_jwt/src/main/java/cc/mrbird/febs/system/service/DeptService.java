package cc.mrbird.febs.system.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.system.domain.DeptScoreRate;
import cc.mrbird.febs.system.domain.User;

public interface DeptService extends IService<Dept> {
    Map<String, Object> findDepts(QueryRequest request, Dept dept);
    IPage<User> findDeptsWithUser(QueryRequest request,  String deptId);
    List<Dept> findDepts(Dept dept, QueryRequest request);
    List<Dept> findThreeDepts();

    void createDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDepts(String[] deptIds);
    public List<DeptScoreRate>  findDeptScoreRate();
    public void updateDeptScoreRate(DeptScoreRate deptScoreRate);
}
