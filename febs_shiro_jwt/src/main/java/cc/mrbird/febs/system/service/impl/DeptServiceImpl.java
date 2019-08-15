package cc.mrbird.febs.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.domain.Tree;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.manager.dao.MtLeaderdepartmentMapper;
import cc.mrbird.febs.manager.dao.StScoretypeMapper;
import cc.mrbird.febs.manager.entity.MtLeaderdepartment;
import cc.mrbird.febs.manager.entity.StScoretype;
import cc.mrbird.febs.system.dao.DeptMapper;
import cc.mrbird.febs.system.dao.UserMapper;
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.system.domain.DeptScoreRate;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.DeptService;
import cc.mrbird.febs.system.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("deptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

	@Autowired
	private UserService userService;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private MtLeaderdepartmentMapper mtLeaderdepartmentMapper;
	@Autowired
	private UserMapper usermapper;

	@Override
	public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Dept> depts = findDepts(dept, request);
			List<Tree<Dept>> trees = new ArrayList<>();
			buildTrees(trees, depts);
			Tree<Dept> deptTree = TreeUtil.build(trees);
			result.put("rows", deptTree);
			result.put("total", depts.size());
		} catch (Exception e) {
			log.error("获取部门列表失败", e);
			result.put("rows", null);
			result.put("total", 0);
		}
		return result;
	}

	@Override
	public IPage<User> findDeptsWithUser(QueryRequest request, String deptId) {
		IPage<User> findByDept = userService.findByDept(deptId, request);
		System.out.println("findByDept的值" + findByDept.getRecords().size());
		return findByDept;
	}

	@Override
	public List<Dept> findDepts(Dept dept, QueryRequest request) {
		QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

		/*
		 * if (StringUtils.isNotBlank(dept.getDeptId().toString()))
		 * queryWrapper.lambda().eq(Dept::getDeptId, dept.getDeptId());
		 */
		if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo()))
			queryWrapper.lambda().ge(Dept::getCreateTime, dept.getCreateTimeFrom()).le(Dept::getCreateTime,
					dept.getCreateTimeTo());
		SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
		List<Dept> selectList = this.baseMapper.selectList(queryWrapper);
		// selectList.remove(0);
		for (Dept dept2 : selectList) {
			if (dept2.getParentId() != 0) {
				String deptParentName = this.baseMapper.selectById(dept2.getParentId()).getDeptName();
				dept2.setDeptParentName(deptParentName);
			}

		}
		return selectList;
	}

	@Override
	public List<DeptScoreRate> findDeptScoreRate() {
		QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().in(Dept::getLevel, 3);
		List<Dept> selectList = this.baseMapper.selectList(queryWrapper);
		List<DeptScoreRate> list = new ArrayList<>();
		for (Dept dept : selectList) {
			DeptScoreRate deptScoreRate = new DeptScoreRate();
			QueryWrapper<MtLeaderdepartment> queryWrapper2 = new QueryWrapper<>();
			queryWrapper2 = new QueryWrapper<>();
			queryWrapper2.lambda().in(MtLeaderdepartment::getDeptId, dept.getDeptId().toString());
			List<MtLeaderdepartment> mtLeaderdepartments = mtLeaderdepartmentMapper.selectList(queryWrapper2);
			QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
			queryWrapper1.lambda().in(User::getLevel, 3);
			queryWrapper1.lambda().in(User::getDeptId, dept.getDeptId());
			User selectOne = usermapper.selectOne(queryWrapper1);
			if (selectOne != null) {

				String name = selectOne.getName();

				deptScoreRate = new DeptScoreRate();
				deptScoreRate.setDeptId(dept.getDeptId());
				deptScoreRate.setRateList(mtLeaderdepartments);
				deptScoreRate.setDeptName(dept.getDeptName());
				deptScoreRate.setUserName(name);
				list.add(deptScoreRate);
			}
		}
		return list;
	}

	@Override
	public List<Dept> findThreeDepts() {

		QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

		/*
		 * if (StringUtils.isNotBlank(dept.getDeptId().toString()))
		 * queryWrapper.lambda().eq(Dept::getDeptId, dept.getDeptId());
		 */

		queryWrapper.lambda().ge(Dept::getLevel, 3);
		/*SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);*/
		List<Dept> selectList = this.baseMapper.selectList(queryWrapper);
		// selectList.remove(0);

		return selectList;
	}

	@Override
	@Transactional
	public void createDept(Dept dept) {
		Long parentId = dept.getParentId();
		if (parentId == null)
			dept.setParentId(0L);
		dept.setCreateTime(new Date());
		this.save(dept);
	}

	@Override
	@Transactional
	public void updateDept(Dept dept) {
		dept.setModifyTime(new Date());
		this.baseMapper.updateById(dept);
	}

	@Override
	@Transactional
	public void updateDeptScoreRate(DeptScoreRate deptScoreRate) {
		MtLeaderdepartment mtLeaderdepartment = null;
		List<MtLeaderdepartment> rateList = deptScoreRate.getRateList();
		for (MtLeaderdepartment mtLeaderdepartment2 : rateList) {
			mtLeaderdepartment = new MtLeaderdepartment();
			mtLeaderdepartment.setDeptId(mtLeaderdepartment2.getDeptId());
			mtLeaderdepartment.setScoreRate(mtLeaderdepartment2.getScoreRate());
			// mtLeaderdepartment.set
			UpdateWrapper<MtLeaderdepartment> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda().in(MtLeaderdepartment::getDeptId, deptScoreRate.getDeptId());
			updateWrapper.lambda().in(MtLeaderdepartment::getUserId, mtLeaderdepartment2.getUserId());
			mtLeaderdepartmentMapper.update(mtLeaderdepartment, updateWrapper);
		}
	}

	@Override
	@Transactional
	public void deleteDepts(String[] deptIds) {
		this.delete(Arrays.asList(deptIds));
	}

	private void buildTreesWithUser(List<Tree<Dept>> trees, List<Dept> depts, QueryRequest page) {
		depts.forEach(dept -> {
			Tree<Dept> tree = new Tree<>();
			tree.setUser(userService.findByDeptId(dept.getDeptId().toString(), page));
			tree.setId(dept.getDeptId().toString());
			tree.setKey(tree.getId());
			tree.setParentId(dept.getParentId().toString());
			tree.setText(dept.getDeptName());
			tree.setCreateTime(dept.getCreateTime());
			tree.setModifyTime(dept.getModifyTime());
			tree.setOrder(dept.getOrderNum());
			// tree.setTitle(tree.getText());
			tree.setValue(tree.getId());
			trees.add(tree);
		});
	}

	private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {
		depts.forEach(dept -> {
			Tree<Dept> tree = new Tree<>();

			tree.setId(dept.getDeptId().toString());
			tree.setLevel(dept.getLevel());
			tree.setKey(tree.getId());
			tree.setParentId(dept.getParentId().toString());
			tree.setText(dept.getDeptName());
			tree.setCreateTime(dept.getCreateTime());
			tree.setModifyTime(dept.getModifyTime());
			tree.setOrder(dept.getOrderNum());
			// tree.setTitle(tree.getText());
			tree.setValue(tree.getId());
			trees.add(tree);
		});
	}

	private void delete(List<String> deptIds) {
		removeByIds(deptIds);

		LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(Dept::getParentId, deptIds);
		List<Dept> depts = baseMapper.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(depts)) {
			List<String> deptIdList = new ArrayList<>();
			depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
			this.delete(deptIdList);
		}
	}
}
